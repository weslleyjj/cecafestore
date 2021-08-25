package br.com.cecafes.controller;

import br.com.cecafes.dto.PedidoDTO;
import br.com.cecafes.dto.PedidoListagemDTO;
import br.com.cecafes.model.*;
import br.com.cecafes.repository.UserRepository;
import br.com.cecafes.security.MyUserDetails;
import br.com.cecafes.service.*;
import br.com.cecafes.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
            return model;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "O pedido não foi encontrado no sistema");
        }
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
            return "produtosListComprador";
        }

        pedidoOficial.setComprador(comprador);
        pedidoOficial.setEndereco(comprador.getEndereco());
        pedidoOficial.setValorPedido(calculaPedido(produtoPedidoList));
        pedidoOficial.setNumero(pedido.getNumero());


        pedidoService.save(pedidoOficial);

        produtoPedidoList.forEach(produtoPedido -> produtoPedido.setPedido(pedidoOficial));

        produtoPedidoService.saveAll(produtoPedidoList);

        pedidoOficial.setProdutosPedido(produtoPedidoList);

        pedidoService.save(pedidoOficial);

        // Remoção do cookie após o pedido finalizado
        Cookie c = CookieUtil.resgatarCookies(cookies, "produtos");
        assert c != null;
        response.addCookie(CookieUtil.removerCookie(c));

        return "index";
    }

    @GetMapping("/gerencia-pedidos")
    public ModelAndView listagemPedidos(){
        ModelAndView model = new ModelAndView("listagemPedidosCecafes");
        List<PedidoListagemDTO> pedidoListagem = new ArrayList<>();
        List<Pedido> pedidoList;

        // Resgata o usuario atual
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();

        try {
            if(!isAdminOrFuncionario(userDetails)){
                User user = userRepository.getUserByUsername(userDetails.getUsername());
                Comprador comprador = compradorService.findByUsername(user.getUsername());
                pedidoList = pedidoService.findPedidosByCompradorId(comprador.getId());
            }else{
                pedidoList = pedidoService.findAll();
            }

            pedidoList.forEach(pedido -> {
                pedidoListagem.add(new PedidoListagemDTO(pedido));
            });

            model.addObject("pedidos", pedidoListagem);
        }catch (Exception e){
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro Durante a consulta de pedidos do usuario", e);
        }

        return model;
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> update(@RequestBody Pedido pedido) {
        Optional<Pedido> pedidoOptional = pedidoService.findById(pedido.getId());

        if (!pedidoOptional.isPresent()) {
            return ResponseEntity.status(404).body(messageService.createJson("message", "Pedido não encontrado"));
        } else {
            return ResponseEntity.ok(pedidoService.save(pedido));
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Pedido> pedidoOptional = pedidoService.findById(id);

        if (!pedidoOptional.isPresent()) {
            return ResponseEntity.status(404).body(messageService.createJson("message", "Pedido não encontrado"));
        } else {
            pedidoService.deleteById(id);
            return ResponseEntity.status(204).build();
        }
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
}
