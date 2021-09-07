package br.com.cecafes.controller;

import br.com.cecafes.model.Produto;
import br.com.cecafes.model.ProdutoCecafes;
import br.com.cecafes.service.MessageService;
import br.com.cecafes.service.ProdutoCecafesService;
import br.com.cecafes.service.ProdutoService;
import br.com.cecafes.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class HomeController {
    private ProdutoService produtoService;
    private ProdutoCecafesService produtoCecafesService;
    private MessageService messageService;

    @Autowired
    public HomeController(ProdutoCecafesService produtoCecafesService, MessageService messageService) {
        this.produtoCecafesService = produtoCecafesService;
        this.messageService = messageService;
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