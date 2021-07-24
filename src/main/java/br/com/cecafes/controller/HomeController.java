package br.com.cecafes.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home() {
        return "index";
    }

    @RequestMapping(value = "/shop", method = RequestMethod.GET)
    public String shop() {
        return "shop-details";
    }

    @RequestMapping(value = "/shop-grid", method = RequestMethod.GET)
    public String shopGrid() {
        return "shop-grid";
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
