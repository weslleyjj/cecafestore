package br.com.cecafes.controller;

import br.com.cecafes.model.Produto;
import br.com.cecafes.service.MessageService;
import br.com.cecafes.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
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
        if (produtoOptional.isEmpty()) {
            return ResponseEntity.status(404).body(messageService.createJson("message", "Produto não encontrado"));
        } else {
            return ResponseEntity.ok(produtoOptional.get());
        }
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody Produto produto) {
        if(validaProduto(produto)){
            return ResponseEntity.status(201).body(produtoService.save(produto));
        }else{
            return ResponseEntity.status(206).body(messageService.createJson("message", "Dados incompletos para cadastro"));
        }

    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> update(@RequestBody Produto produto) {
        Optional<Produto> produtoOptional = produtoService.findById(produto.getId());

        if (produtoOptional.isEmpty()) {
            return ResponseEntity.status(404).body(messageService.createJson("message", "Produto não encontrado"));
        } else {
            if(validaProduto(produto)){
                return ResponseEntity.ok(produtoService.save(produto));
            }else{
                return ResponseEntity.status(206).body(messageService.createJson("message", "Dados incompletos para cadastro"));
            }

        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Produto> produtoOptional = produtoService.findById(id);

        if (produtoOptional.isEmpty()) {
            return ResponseEntity.status(404).body(messageService.createJson("message", "Produto não encontrado"));
        } else {
            produtoService.deleteById(id);
            return ResponseEntity.status(204).build();
        }
    }

    private boolean validaProduto(Produto produto){
        return (Objects.nonNull(produto.getCategoria()) || produto.getCategoria().isBlank()) && Objects.nonNull(produto.getDataValidade())
                && (Objects.nonNull(produto.getNome()) || produto.getNome().isBlank()) && Objects.nonNull(produto.getQtdCaixa());

    }
}
