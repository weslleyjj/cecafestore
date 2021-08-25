package br.com.cecafes.controller;

import br.com.cecafes.model.Produto;
import br.com.cecafes.model.ProdutoCecafes;
import br.com.cecafes.service.MessageService;
import br.com.cecafes.service.ProdutoCecafesService;
import br.com.cecafes.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
        Cookie[] cookiesRequest = request.getCookies();
        if ( !verificaCookies(cookiesRequest, "produtos") ){
            Cookie c = new Cookie("produtos", "");
            c.setMaxAge(60 * 60 * 24 * 30); // 30 dias
            response.addCookie(c);
        }

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
        ModelAndView modelAndView = new ModelAndView("shop-details");
        ProdutoCecafes produto = produtoCecafesService.findById(id);
        modelAndView.addObject("produto", produto);

        return modelAndView;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String adicionarCarrinho(@PathVariable(name = "id") Long id, HttpServletResponse response, HttpServletRequest request) {
        Cookie[] cookiesRequest = request.getCookies();

        if (verificaCookies(cookiesRequest, "produtos")){
            Cookie cookie = procurarCookie(cookiesRequest, "produtos");

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

    private boolean verificaCookies(Cookie[] cookies, String cookieName){
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals(cookieName)){
                return true;
            }
        }
        return false;
    }

    private Cookie procurarCookie(Cookie[] cookies, String cookieName){
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)){
                return cookie;
            }
        }

        return null;
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