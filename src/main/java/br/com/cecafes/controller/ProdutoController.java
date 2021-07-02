package br.com.cecafes.controller;

import br.com.cecafes.model.Produto;
import br.com.cecafes.service.MessageService;
import br.com.cecafes.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/produto")
public class ProdutoController {
    private ProdutoService produtoService;
    private MessageService messageService;

    @Autowired
    public ProdutoController(ProdutoService produtoService, MessageService messageService) {
        this.produtoService = produtoService;
        this.messageService = messageService;
    }

    @GetMapping
    public List<Produto> findAll() {
        return produtoService.findAll();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<Produto> produtoOptional = produtoService.findById(id);
        if (!produtoOptional.isPresent()) {
            return ResponseEntity.status(404).body(messageService.createJson("message", "Produto não encontrado"));
        } else {
            return ResponseEntity.ok(produtoOptional.get());
        }
    }

    @PostMapping
    public ResponseEntity<Produto> save(@RequestBody Produto produto) {
        return ResponseEntity.status(201).body(produtoService.save(produto));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> update(@RequestBody Produto produto) {
        Optional<Produto> produtoOptional = produtoService.findById(produto.getId());

        if (!produtoOptional.isPresent()) {
            return ResponseEntity.status(404).body(messageService.createJson("message", "Produto não encontrado"));
        } else {
            return ResponseEntity.ok(produtoService.save(produto));
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Produto> produtoOptional = produtoService.findById(id);

        if (!produtoOptional.isPresent()) {
            return ResponseEntity.status(404).body(messageService.createJson("message", "Produto não encontrado"));
        } else {
            produtoService.deleteById(id);
            return ResponseEntity.status(204).build();
        }
    }
}
