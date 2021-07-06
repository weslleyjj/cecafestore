package br.com.cecafes.controller;

import br.com.cecafes.model.FuncionarioCecafes;
import br.com.cecafes.service.FuncionarioCecafesService;
import br.com.cecafes.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/funcionario")
public class FuncionarioCecafesController {
    private FuncionarioCecafesService funcionarioCecafesService;
    private MessageService messageService;

    @Autowired
    public FuncionarioCecafesController(FuncionarioCecafesService funcionarioCecafesService, MessageService messageService) {
        this.funcionarioCecafesService = funcionarioCecafesService;
        this.messageService = messageService;
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
    public ResponseEntity<FuncionarioCecafes> save(@RequestBody FuncionarioCecafes pedido) {
        return ResponseEntity.status(201).body(funcionarioCecafesService.save(pedido));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> update(@RequestBody FuncionarioCecafes pedido) {
        Optional<FuncionarioCecafes> funcionarioCecafesOptional = funcionarioCecafesService.findById(pedido.getId());

        if (!funcionarioCecafesOptional.isPresent()) {
            return ResponseEntity.status(404).body(messageService.createJson("message", "FuncionarioCecafes não encontrado"));
        } else {
            return ResponseEntity.ok(funcionarioCecafesService.save(pedido));
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
