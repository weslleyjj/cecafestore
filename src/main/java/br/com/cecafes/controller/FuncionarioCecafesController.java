package br.com.cecafes.controller;

import br.com.cecafes.model.FuncionarioCecafes;
import br.com.cecafes.service.FuncionarioCecafesService;
import br.com.cecafes.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/funcionario")
public class FuncionarioCecafesController {
    private FuncionarioCecafesService funcionarioCecafesService;
    private PasswordEncoder passwordEncoder;
    private MessageService messageService;

    @Autowired
    public FuncionarioCecafesController(FuncionarioCecafesService funcionarioCecafesService, MessageService messageService, PasswordEncoder passwordEncoder) {
        this.funcionarioCecafesService = funcionarioCecafesService;
        this.messageService = messageService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public List<FuncionarioCecafes> findAll() {
        return funcionarioCecafesService.findAll();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<FuncionarioCecafes> funcionarioCecafesOptional = funcionarioCecafesService.findById(id);
        if (!funcionarioCecafesOptional.isPresent()) {
            return ResponseEntity.status(404).body(messageService.createJson("message", "FuncionarioCecafes não encontrado"));
        } else {
            return ResponseEntity.ok(funcionarioCecafesOptional.get());
        }
    }

    @PostMapping
    public ResponseEntity<FuncionarioCecafes> save(@RequestBody FuncionarioCecafes funcionarioCecafes) {
        funcionarioCecafes.setSenha(passwordEncoder.encode(funcionarioCecafes.getSenha()));
        return ResponseEntity.status(201).body(funcionarioCecafesService.save(funcionarioCecafes));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> update(@RequestBody FuncionarioCecafes funcionarioCecafes) {
        Optional<FuncionarioCecafes> funcionarioCecafesOptional = funcionarioCecafesService.findById(funcionarioCecafes.getId());

        if (!funcionarioCecafesOptional.isPresent()) {
            return ResponseEntity.status(404).body(messageService.createJson("message", "FuncionarioCecafes não encontrado"));
        } else {
            funcionarioCecafes.setSenha(passwordEncoder.encode(funcionarioCecafes.getSenha()));
            return ResponseEntity.ok(funcionarioCecafesService.save(funcionarioCecafes));
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<FuncionarioCecafes> funcionarioCecafesOptional = funcionarioCecafesService.findById(id);

        if (!funcionarioCecafesOptional.isPresent()) {
            return ResponseEntity.status(404).body(messageService.createJson("message", "FuncionarioCecafes não encontrado"));
        } else {
            funcionarioCecafesService.deleteById(id);
            return ResponseEntity.status(204).build();
        }
    }
}
