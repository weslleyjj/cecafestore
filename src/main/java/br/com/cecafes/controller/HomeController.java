package br.com.cecafes.controller;

import br.com.cecafes.model.*;
import br.com.cecafes.repository.UserRepository;
import br.com.cecafes.security.MyUserDetails;
import br.com.cecafes.service.*;
import br.com.cecafes.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class HomeController {
    private ProdutoService produtoService;
    private ProdutoCecafesService produtoCecafesService;
    private MessageService messageService;
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private ProdutorService produtorService;
    private FuncionarioCecafesService funcionarioCecafesService;
    private CompradorService compradorService;

    @Autowired
    public HomeController(ProdutoCecafesService produtoCecafesService, MessageService messageService, UserRepository userRepository, ProdutorService produtorService, CompradorService compradorService, FuncionarioCecafesService funcionarioCecafesService, PasswordEncoder passwordEncoder) {
        this.produtoCecafesService = produtoCecafesService;
        this.messageService = messageService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.produtorService = produtorService;
        this.funcionarioCecafesService = funcionarioCecafesService;
        this.compradorService = compradorService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView home(HttpServletResponse response, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("index");
        List<ProdutoCecafes> produtos = produtoCecafesService.findAll();
        modelAndView.addObject("produtos", produtos);

        return modelAndView;
    }

    @RequestMapping(value = "/cadastro-sistema", method = RequestMethod.GET)
    public String escolhaCadastro() {return "escolha-cadastro";}

    @RequestMapping(value = "/shop", method = RequestMethod.GET)
    public String shop() {
        return "shop-details-template";
    }

    @RequestMapping(value = "/shop/{id}", method = RequestMethod.GET)
    public ModelAndView shop(@PathVariable(name = "id") Long id) {
        Random random = new Random();
        ArrayList<ProdutoCecafes> produtosRelacionados = new ArrayList<>();

        ModelAndView modelAndView = new ModelAndView("shop-details");
        ProdutoCecafes produto = produtoCecafesService.findById(id);
        List<ProdutoCecafes> produtos = produtoCecafesService.findAll();

        for(int i = 0; i < produtos.size(); i++){
            int num = random.nextInt(produtos.size());

            if(!produtosRelacionados.contains(produtos.get(num))){
                produtosRelacionados.add(produtos.get(num));
            }

            if(produtosRelacionados.size() == 4){
                break;
            }
        }

        modelAndView.addObject("produto", produto);
        modelAndView.addObject("produtosRelacionados", produtosRelacionados);

        return modelAndView;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String adicionarCarrinho(@PathVariable(name = "id") Long id, HttpServletResponse response, HttpServletRequest request) {
        Cookie[] cookiesRequest = request.getCookies();

        if (CookieUtil.verificaCookies(cookiesRequest, "produtos")){
            Cookie cookie = CookieUtil.procurarCookie(cookiesRequest, "produtos");

            String valorCookie = cookie.getValue();

            if(valorCookie.indexOf(id.toString()) == -1){
                valorCookie += id + "/";

                cookie.setValue(valorCookie);
                cookie.setMaxAge(60 * 60 * 24 * 30);
                response.addCookie(cookie);
            }

        }

        return "redirect:/";
    }

    @RequestMapping(value = "/busca-produto", method = RequestMethod.GET)
    public String buscaProduto(@RequestParam String nome, Model model) {
        List<ProdutoCecafes> produtosCecafes = produtoCecafesService.findAll();
        List<ProdutoCecafes> produtosCecafesBusca = new ArrayList<>();

        Pattern pattern = Pattern.compile(nome, Pattern.CASE_INSENSITIVE);

        if (!produtosCecafes.isEmpty()) {
            Matcher matcher;
            boolean matchFound;
            for(ProdutoCecafes produto: produtosCecafes) {
                matcher = pattern.matcher(produto.getNome() + " " + produto.getCategoria());
                matchFound = matcher.find();

                if (matchFound) {
                    produtosCecafesBusca.add(produto);
                }
            }
        }

        model.addAttribute("produtos", produtosCecafesBusca);
        return "busca";
    }

    @RequestMapping(value = "/detalhesUsuario")
    public ModelAndView detalhesUsuario() {
        ModelAndView modelAndView;
        User user = getUser();

        if (user.getRoles().iterator().next().getName().equals("PRODUTOR")) {
            modelAndView = new ModelAndView("detalhesUsuario");
            Produtor produtor = getProdutor(user);
            modelAndView.addObject("usuario", produtor);

        } else if (user.getRoles().iterator().next().getName().equals("COMPRADOR")) {
            modelAndView = new ModelAndView("detalhesUsuario");
            Comprador comprador = getComprador(user);
            modelAndView.addObject("usuario", comprador);

        } else if (user.getRoles().iterator().next().getName().equals("FUNCIONARIO")) {
            modelAndView = new ModelAndView("detalhesFuncionario");
            FuncionarioCecafes funcionarioCecafes = getFuncionario(user);
            modelAndView.addObject("usuario", funcionarioCecafes);

        } else {
            modelAndView = new ModelAndView("index");
        }

        return modelAndView;
    }

    @RequestMapping(value = "/saveFuncionarioEdit")
    public String saveFuncionarioEdit(@ModelAttribute @Valid FuncionarioCecafes funcionarioCecafes) {
        User user = getUser();

        funcionarioCecafes.getUser().setPassword(passwordEncoder.encode(funcionarioCecafes.getUser().getPassword()));
        Set<Role> papel = new HashSet<>();
        papel.add(new Role().builder().id(3).name("FUNCIONARIO").build());
        funcionarioCecafes.getUser().setRoles(papel);
        funcionarioCecafes.getUser().setEnabled(true);

        funcionarioCecafes.getUser().setId(user.getId());

        userRepository.save(funcionarioCecafes.getUser());
        funcionarioCecafesService.save(funcionarioCecafes);

        return "redirect:/detalhesUsuario";
    }

    @RequestMapping(value = "/saveCompradorEdit")
    public String saveCompradorEdit(@ModelAttribute @Valid Comprador comprador) {
        User user = getUser();

        comprador.getUser().setPassword(passwordEncoder.encode(comprador.getUser().getPassword()));
        Set<Role> papel = new HashSet<>();
        papel.add(new Role().builder().id(2).name("COMPRADOR").build());
        comprador.getUser().setRoles(papel);
        comprador.getUser().setEnabled(true);

        comprador.getUser().setId(user.getId());

        userRepository.save(comprador.getUser());
        compradorService.save(comprador);

        return "redirect:/detalhesUsuario";
    }

    @RequestMapping(value = "/saveProdutorEdit")
    public String saveProdutorEdit(@ModelAttribute @Valid Produtor produtor) {
        User user = getUser();

        produtor.getUser().setPassword(passwordEncoder.encode(produtor.getUser().getPassword()));
        Set<Role> papel = new HashSet<>();
        papel.add(new Role().builder().id(1).name("PRODUTOR").build());
        produtor.getUser().setRoles(papel);
        produtor.getUser().setEnabled(true);

        produtor.getUser().setId(user.getId());

        userRepository.save(produtor.getUser());
        produtorService.save(produtor);

        return "redirect:/detalhesUsuario";
    }

    @RequestMapping(value = "/editarUsuario")
    public ModelAndView editarUsuario() {
        ModelAndView modelAndView;
        User user = getUser();

        if (user.getRoles().iterator().next().getName().equals("PRODUTOR")) {
            modelAndView = new ModelAndView("editarProdutor");
            Produtor produtor = getProdutor(user);
            modelAndView.addObject("usuario", produtor);

        } else if (user.getRoles().iterator().next().getName().equals("COMPRADOR")) {
            modelAndView = new ModelAndView("editarComprador");
            Comprador comprador = getComprador(user);
            modelAndView.addObject("usuario", comprador);

        } else if (user.getRoles().iterator().next().getName().equals("FUNCIONARIO")) {
            modelAndView = new ModelAndView("editarFuncionario");
            FuncionarioCecafes funcionarioCecafes = getFuncionario(user);
            modelAndView.addObject("usuario", funcionarioCecafes);

        } else {
            modelAndView = new ModelAndView("index");
        }

        return modelAndView;
    }

    @RequestMapping(value = "/shop-grid", method = RequestMethod.GET)
    public String shopGrid() {
        return "shop-grid-template";
    }

    @RequestMapping(value = "/shop-cart", method = RequestMethod.GET)
    public String shopCart() {
        return "shoping-cart";
    }

    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public String main() {
        return "main";
    }

    @RequestMapping(value = "/contact", method = RequestMethod.GET)
    public String contact() {
        return "contact";
    }

    @RequestMapping(value = "/checkout", method = RequestMethod.GET)
    public String checkout() {
        return "checkout";
    }

    @RequestMapping(value = "/blog-details", method = RequestMethod.GET)
    public String blogDetails() {
        return "blog-details";
    }

    @RequestMapping(value = "/blog", method = RequestMethod.GET)
    public String blog() {
        return "blog";
    }

    private User getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        User user = userRepository.getUserByUsername(userDetails.getUsername());

        return user;
    }

    private Produtor getProdutor(User user){
        Produtor produtor = produtorService.findByUsername(user.getUsername());

        return produtor;
    }

    private FuncionarioCecafes getFuncionario(User user){
        FuncionarioCecafes funcionarioCecafes = funcionarioCecafesService.findByUsername(user.getUsername());

        return funcionarioCecafes;
    }

    private Comprador getComprador(User user){
        Comprador comprador = compradorService.findByUsername(user.getUsername());

        return comprador;
    }
}

/*

Index: Modelo com listagem de imagens, cards, slides

Shop-details: Modelo com detalhamento e abas de informações diferentes

Shop-grid: Modelo com mais listagens e slides

Shopping-cart: Tela com lista por linha, opção de alteração de 1 propriedade, e resumo de dados manipulados

Contact: Form de comunicação, iframe e apresentação breve de informações

Checkout: form e resumo de dados

Blog-details: tags, aside com links, informações de autor

Blog: links, cards e paginação

*/