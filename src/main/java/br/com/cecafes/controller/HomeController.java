package br.com.cecafes.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class HomeController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(HttpServletResponse response, HttpServletRequest request) {
        Cookie[] cookiesRequest = request.getCookies();
        if ( !verificaCookies(cookiesRequest, "produtos") ){
            Cookie c = new Cookie("produtos", "");
            c.setMaxAge(60 * 60 * 24 * 30); // 30 dias
            response.addCookie(c);
        }

        return "index";
    }

    @RequestMapping(value = "/cadastro-sistema", method = RequestMethod.GET)
    public String escolhaCadastro() {return "escolha-cadastro";}

    @RequestMapping(value = "/shop", method = RequestMethod.GET)
    public String shop() {
        return "shop-details-template";
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