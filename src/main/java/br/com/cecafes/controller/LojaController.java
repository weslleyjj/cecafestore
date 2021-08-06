package br.com.cecafes.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/loja")
public class LojaController {

    @GetMapping
    public String homeLoja() {
        return "shop-grid";
    }
}
