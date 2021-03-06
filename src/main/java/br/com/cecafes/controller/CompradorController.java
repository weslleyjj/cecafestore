package br.com.cecafes.controller;

import br.com.cecafes.dto.PedidoDTO;
import br.com.cecafes.model.*;
import br.com.cecafes.repository.ProdutoCecafesRepository;
import br.com.cecafes.repository.UserRepository;
import br.com.cecafes.service.MessageService;
import br.com.cecafes.service.CompradorService;
import br.com.cecafes.service.ProdutoCecafesService;
import br.com.cecafes.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/comprador")
public class CompradorController {
    private CompradorService compradorService;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private MessageService messageService;
    private ProdutoCecafesService produtoCecafesService;

    @Autowired
    public CompradorController(
            CompradorService compradorService,
            MessageService messageService,
            PasswordEncoder passwordEncoder,
            UserRepository userRepository,
            ProdutoCecafesService produtoCecafesService,
            ProdutoCecafesRepository produtoCecafesRepository
    ) {
        this.compradorService = compradorService;
        this.messageService = messageService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.produtoCecafesService = produtoCecafesService;
    }

    @GetMapping
    public List<Comprador> findAll() {
        return compradorService.findAll();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Comprador comprador = compradorService.findById(id);
        if (Objects.isNull(comprador)) {
            return ResponseEntity.status(404).body(messageService.createJson("message", "Comprador n??o encontrado"));
        } else {
            return ResponseEntity.ok(comprador);
        }
    }

    @PostMapping(value = "/cadastrar")
    public String save(@ModelAttribute @Valid Comprador comprador, Errors errors) {
        if(!errors.hasErrors()){
            comprador.getUser().setPassword(passwordEncoder.encode(comprador.getUser().getPassword()));

            // Definindo a Role do comprador no cadastro do mesmo
            Set<Role> papel = new HashSet<>();
            papel.add(new Role().builder().id(2).name("COMPRADOR").build());
            comprador.getUser().setRoles(papel);
            comprador.getUser().setEnabled(true);

            userRepository.save(comprador.getUser());
            compradorService.save(comprador);
            return "redirect:/";
        }

        return "formComprador";
    }

    @GetMapping(value = "/form-comprador")
    public String formComprador(Model model){
        model.addAttribute("comprador", new Comprador());

        return "formComprador";
    }

    @GetMapping(value = "/add-cart")
    public String addCart(@RequestParam(name = "id") Long id, HttpServletRequest request, HttpServletResponse response) {
        ProdutoCecafes produto = produtoCecafesService.findById(id);

        if (Objects.isNull(produto)) {
            return "error";
        }

        Cookie cookies[] = request.getCookies();

        Cookie cookieProduto = null;
        boolean flag = false;
        for (Cookie cookie: cookies) {
            if (cookie.getName().equals("produtos")) {
                cookieProduto = cookie;
                flag = true;
                break;
            }
        }

        if (flag) {
            cookieProduto.setValue(cookieProduto.getValue() + "/" + produto.getId().toString());
        } else {
            cookieProduto = new Cookie("produtos", produto.getId().toString() + "/");
        }

        cookieProduto.setMaxAge(60 * 60 * 24 * 30);
        response.addCookie(cookieProduto);

        return "redirect:/";
    }

    @GetMapping(value = "/lista-produtos-compra")
    public ModelAndView listProdutosCompra(Model model, HttpServletRequest request, Authentication authentication) {
        Cookie[] cookies = request.getCookies();
        Cookie produtos = CookieUtil.resgatarCookies(cookies, "produtos");
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Comprador comprador = compradorService.findByUsername(userDetails.getUsername());
        PedidoDTO pedido = new PedidoDTO();

        ModelAndView modelAndView = new ModelAndView("listagemCarrinhoComprador");
        List<ProdutoCecafes> produtosList = new ArrayList<>();
        String numeroPedido = "";

        // Resgata todos os produtos contidos no cookie
        try {
            if (!Objects.isNull(produtos)){
                String[] produtosIds = produtos.getValue().split("/");
                for (String produtoId : produtosIds) {
                    ProdutoCecafes produtoTemp = null;
                    try{
                        if (!produtoId.equals("")) {
                            produtoTemp = produtoCecafesService.findById(Long.parseLong(produtoId));
                        }
                    }catch (NoSuchElementException nse){
                        System.err.println("Produto referenciado no cookie nao foi encontrado");
                        System.err.println(nse);
                        continue;
                    }
                    if(Objects.nonNull(produtoTemp)){
                        produtosList.add(produtoTemp);
                        numeroPedido += produtoTemp.getId();
                    }
                }
            }
        }catch (Exception e){
            System.err.println(e);
        }

        if(!Objects.isNull(comprador)){
            LocalDateTime date = LocalDateTime.now() ;
            numeroPedido = "";
            numeroPedido += date.getMonthValue();
            numeroPedido += date.getDayOfMonth();
            numeroPedido += date.getHour();
            numeroPedido += date.getMinute();
            numeroPedido += date.getSecond();
            numeroPedido += comprador.getId();
            pedido.setComprador(comprador);
        }

        pedido.setProdutos(produtosList);
        pedido.setNumero(numeroPedido);

        modelAndView.addObject("pedido", pedido);

        return modelAndView;
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> update(@RequestBody Comprador comprador) {
        Comprador compradorUpdate = compradorService.findById(comprador.getId());

        if (Objects.isNull(compradorUpdate)) {
            return ResponseEntity.status(404).body(messageService.createJson("message", "Comprador n??o encontrado"));
        } else {
            comprador.getUser().setPassword(passwordEncoder.encode(comprador.getUser().getPassword()));
            return ResponseEntity.ok(compradorService.save(comprador));
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Comprador compradorDelete = compradorService.findById(id);

        if (Objects.isNull(compradorDelete)) {
            return ResponseEntity.status(404).body(messageService.createJson("message", "Comprador n??o encontrado"));
        } else {
            compradorService.deleteById(id);
            return ResponseEntity.status(204).build();
        }
    }

}
