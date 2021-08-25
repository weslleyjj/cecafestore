package br.com.cecafes.controller.rest;

import br.com.cecafes.util.CookieUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController(value = "/loja")
public class LojaRestController {

    @GetMapping("/add-item/{id}")
    public ResponseEntity<?> adicionarCarrinho(@PathVariable(name = "id") Long id, HttpServletResponse response, HttpServletRequest request) {
        Cookie[] cookiesRequest = request.getCookies();

        try{
            if (CookieUtil.verificaCookies(cookiesRequest, "produtos")){
                Cookie cookie = CookieUtil.procurarCookie(cookiesRequest, "produtos");

                String valorCookie = cookie.getValue();

                if(!valorCookie.contains(id.toString())){
                    valorCookie += id + "/";

                    cookie.setValue(valorCookie);
                    cookie.setMaxAge(60 * 60 * 24 * 30);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }

            }else{
                Cookie c = new Cookie("produtos", id+"/");
                c.setMaxAge(60 * 60 * 24 * 30); // 30 dias
                c.setPath("/");
                response.addCookie(c);
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }


        return ResponseEntity.ok("produto adicionado");
    }
}
