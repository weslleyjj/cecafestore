package br.com.cecafes.controller;

import br.com.cecafes.model.Comprador;
import br.com.cecafes.model.Produtor;
import br.com.cecafes.service.MessageService;
import br.com.cecafes.service.CompradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/comprador")
public class CompradorController {
    private CompradorService compradorService;
    private PasswordEncoder passwordEncoder;
    private MessageService messageService;

    @Autowired
    public CompradorController(CompradorService compradorService, MessageService messageService, PasswordEncoder passwordEncoder) {
        this.compradorService = compradorService;
        this.messageService = messageService;
        this.passwordEncoder = passwordEncoder;
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
    public String save(@ModelAttribute @Valid Comprador comprador) {
        comprador.setSenha(passwordEncoder.encode(comprador.getSenha()));
        compradorService.save(comprador);

        return "redirect:/";
    }

    @GetMapping(value = "/form-comprador")
    public String formComprador(Model model){
        model.addAttribute("comprador", new Comprador());

        return "formComprador";
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> update(@RequestBody Comprador comprador) {
        Optional<Comprador> compradorOptional = compradorService.findById(comprador.getId());

        if (!compradorOptional.isPresent()) {
            return ResponseEntity.status(404).body(messageService.createJson("message", "Comprador não encontrado"));
        } else {
            comprador.setSenha(passwordEncoder.encode(comprador.getSenha()));
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
