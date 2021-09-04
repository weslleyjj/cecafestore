package br.com.cecafes.controller;

import br.com.cecafes.dto.PedidoDTO;
import br.com.cecafes.model.*;
import br.com.cecafes.repository.UserRepository;
import br.com.cecafes.security.MyUserDetails;
import br.com.cecafes.service.*;
import br.com.cecafes.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/pedido")
public class PedidoController {
    private PedidoService pedidoService;
    private MessageService messageService;
    private ProdutoCecafesService produtoCecafesService;
    private CompradorService compradorService;
    private ProdutoPedidoService produtoPedidoService;
    private UserRepository userRepository;

    @Autowired
    public PedidoController(PedidoService pedidoService, MessageService messageService, ProdutoCecafesService produtoCecafesService, CompradorService compradorService, ProdutoPedidoService produtoPedidoService, UserRepository userRepository) {
        this.pedidoService = pedidoService;
        this.messageService = messageService;
        this.produtoCecafesService = produtoCecafesService;
        this.compradorService = compradorService;
        this.produtoPedidoService = produtoPedidoService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Pedido> findAll() {
        return pedidoService.findAll();
    }

    @GetMapping(value = "/{id}")
    public ModelAndView findById(@PathVariable Long id) {
        Optional<Pedido> pedidoOptional = pedidoService.findById(id);
        if (pedidoOptional.isPresent()) {
            ModelAndView model = new ModelAndView("detalhesPedido");
            model.addObject("pedido", pedidoOptional.get());
            model.addObject("corStatus", corStatus(pedidoOptional.get().getStatus()));
            return model;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "O pedido não foi encontrado no sistema");
        }
    }

    @GetMapping(value = "/revisar/{id}/{evento}")
    public String revisarPedido(@PathVariable Long id, @PathVariable String evento){
        try{
            Pedido pedido = pedidoService.findById(id).get();
            if(Objects.nonNull(pedido)){
                pedido.setStatus(evento.toUpperCase());
                pedidoService.save(pedido);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "redirect:/pedido/"+id;
    }

    @PostMapping
    public String save(@ModelAttribute @Valid PedidoDTO pedido, HttpServletRequest request, HttpServletResponse response) {
        List<ProdutoPedido> produtoPedidoList = new ArrayList<>();
        Pedido pedidoOficial = new Pedido();
        Cookie[] cookies = request.getCookies();

        List<Long> idsProdutos = new ArrayList<>();
        List<ProdutoCecafes> produtosAnteriores = pedido.getProdutos();

        pedido.getProdutos().forEach(produto -> {
            idsProdutos.add(produto.getId());
        });

        pedido.setProdutos(produtoCecafesService.findProdutosByIds(idsProdutos));

        for(int i = 0; i < pedido.getProdutos().size() ; i++){

                Long idTmp = produtosAnteriores.get(i).getId();
                int qtd = produtosAnteriores.get(i).getQuantidade();

                if(pedido.getProdutos().stream().anyMatch(pId -> Objects.equals(pId.getId(), idTmp))){
                    for (ProdutoCecafes produto : pedido.getProdutos()) {
                        if(Objects.equals(produto.getId(), idTmp)){
                            pedido.getProdutos().get(i).setQuantidade(qtd);
                        }
                    }
                }

                if(pedido.getProdutos().stream().anyMatch(pId -> pId.getId() == idTmp)){
                    pedido.getProdutos().get(i).setQuantidade(qtd);
                }

                produtoPedidoList.add(new ProdutoPedido(pedido.getProdutos().get(i)));
        }

        Comprador comprador = compradorService.findById(pedido.getComprador().getId());

        // Caso seja admin ou outra role, nao ira cumprir a operaçao
        if(Objects.isNull(comprador)){
            return "listagemCarrinhoComprador";
        }

        pedidoOficial.setComprador(comprador);
        pedidoOficial.setEndereco(comprador.getEndereco());
        pedidoOficial.setValorPedido(calculaPedido(produtoPedidoList));
        pedidoOficial.setNumero(pedido.getNumero());
        pedidoOficial.setStatus("PENDENTE");


        pedidoService.save(pedidoOficial);

        produtoPedidoList.forEach(produtoPedido -> produtoPedido.setPedido(pedidoOficial));

        produtoPedidoService.saveAll(produtoPedidoList);

        pedidoOficial.setProdutosPedido(produtoPedidoList);

        pedidoService.save(pedidoOficial);

        // Remoção do cookie após o pedido finalizado
        Cookie c = CookieUtil.resgatarCookies(cookies, "produtos");
        assert c != null;
        response.addCookie(CookieUtil.removerCookie(c));

        return "redirect:/pedido/"+pedidoOficial.getId();
    }

    @RequestMapping(value = "/pedidos", method = RequestMethod.GET)
    public String listPedidos(
            Model model,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Page<Pedido> pedidoPage;

        // Resgata o usuario atual
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();

        if(!isAdminOrFuncionario(userDetails)){
            User user = userRepository.getUserByUsername(userDetails.getUsername());
            Comprador comprador = compradorService.findByUsername(user.getUsername());
            pedidoPage = pedidoService.findPaginated(PageRequest.of(currentPage - 1, pageSize), comprador.getId());
        }else{
            pedidoPage = pedidoService.findPaginated(PageRequest.of(currentPage - 1, pageSize), null);
        }

        model.addAttribute("pedidoPage", pedidoPage);

        int totalPages = pedidoPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "listagemPedidosCecafes";
    }

    @RequestMapping(value = "/pedidos-em-aberto", method = RequestMethod.GET)
    public String listPedidosEmAberto(
            Model model,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Page<Pedido> pedidoPage;

        pedidoPage = pedidoService.findPaginatedEmAberto(PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("pedidoPage", pedidoPage);

        int totalPages = pedidoPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "listagemPedidosCecafes";
    }

    @GetMapping(value = "/busca-pedido")
    private String buscaPedido(@RequestParam String numero, Model model) {
        List<Pedido> pedidos = pedidoService.findByNumero(numero.trim());
        model.addAttribute("pedidos", pedidos);

        return "busca-pedido";
    }

    private Float calculaPedido(List<ProdutoPedido> produtos){
        Float valorTotal = 0f;
        for (ProdutoPedido produto : produtos) {
            valorTotal += Float.parseFloat(produto.getPreco().replace(',', '.')) * produto.getQuantidade();
        }

        return BigDecimal.valueOf(valorTotal).setScale(2, RoundingMode.HALF_DOWN).floatValue();
    }

    private boolean isAdminOrFuncionario(MyUserDetails user){
        for (GrantedAuthority authority : user.getAuthorities()) {
            if(authority.getAuthority().equalsIgnoreCase("admin")
                || authority.getAuthority().equalsIgnoreCase("funcionario")){
                return true;
            }
        }
        return false;
    }

    private String corStatus(String status){
        switch (status) {
            case "PENDENTE": return "darkorange";
            case "REVISADO": return "#17a2b8";
            case "CONFIRMADO": return "#7fad39";
            case "NEGADO": return "tomato";
            default: return "transparent";
        }
    }
}