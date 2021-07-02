package br.com.cecafes.controller;

import br.com.cecafes.model.Endereco;
import br.com.cecafes.service.MessageService;
import br.com.cecafes.service.EnderecoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/endereco")
public class EnderecoController {
    private EnderecoService enderecoService;
    private MessageService messageService;

    @Autowired
    public EnderecoController(EnderecoService enderecoService, MessageService messageService) {
        this.enderecoService = enderecoService;
        this.messageService = messageService;
    }

    @GetMapping
    public List<Endereco> findAll() {
        return enderecoService.findAll();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<Endereco> enderecoOptional = enderecoService.findById(id);
        if (!enderecoOptional.isPresent()) {
            return ResponseEntity.status(404).body(messageService.createJson("message", "Endereco não encontrado"));
        } else {
            return ResponseEntity.ok(enderecoOptional.get());
        }
    }

    @PostMapping
    public ResponseEntity<Endereco> save(@RequestBody Endereco endereco) {
        return ResponseEntity.status(201).body(enderecoService.save(endereco));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> update(@RequestBody Endereco endereco) {
        Optional<Endereco> enderecoOptional = enderecoService.findById(endereco.getId());

        if (!enderecoOptional.isPresent()) {
            return ResponseEntity.status(404).body(messageService.createJson("message", "Endereco não encontrado"));
        } else {
            return ResponseEntity.ok(enderecoService.save(endereco));
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Endereco> enderecoOptional = enderecoService.findById(id);

        if (!enderecoOptional.isPresent()) {
            return ResponseEntity.status(404).body(messageService.createJson("message", "Endereco não encontrado"));
        } else {
            enderecoService.deleteById(id);
            return ResponseEntity.status(204).build();
        }
    }
}
