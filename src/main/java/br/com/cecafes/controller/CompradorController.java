package br.com.cecafes.controller;

import br.com.cecafes.model.Comprador;
import br.com.cecafes.model.Produtor;
import br.com.cecafes.model.Role;
import br.com.cecafes.repository.UserRepository;
import br.com.cecafes.service.MessageService;
import br.com.cecafes.service.CompradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/comprador")
public class CompradorController {
    private CompradorService compradorService;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private MessageService messageService;

    @Autowired
    public CompradorController(CompradorService compradorService, MessageService messageService, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.compradorService = compradorService;
        this.messageService = messageService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Comprador> findAll() {
        return compradorService.findAll();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<Comprador> compradorOptional = compradorService.findById(id);
        if (!compradorOptional.isPresent()) {
            return ResponseEntity.status(404).body(messageService.createJson("message", "Comprador não encontrado"));
        } else {
            return ResponseEntity.ok(compradorOptional.get());
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

    @GetMapping(value = "lista-produtos-compra")
    public String listProdutosCompra(Model model, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        for (Cookie cookie : cookies) {
            System.out.println(cookie.getName() +"      =       "+ cookie.getValue());
        }

        return "produtosListComprador";
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> update(@RequestBody Comprador comprador) {
        Optional<Comprador> compradorOptional = compradorService.findById(comprador.getId());

        if (!compradorOptional.isPresent()) {
            return ResponseEntity.status(404).body(messageService.createJson("message", "Comprador não encontrado"));
        } else {
            comprador.getUser().setPassword(passwordEncoder.encode(comprador.getUser().getPassword()));
            return ResponseEntity.ok(compradorService.save(comprador));
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Comprador> compradorOptional = compradorService.findById(id);

        if (!compradorOptional.isPresent()) {
            return ResponseEntity.status(404).body(messageService.createJson("message", "Comprador não encontrado"));
        } else {
            compradorService.deleteById(id);
            return ResponseEntity.status(204).build();
        }
    }
}
