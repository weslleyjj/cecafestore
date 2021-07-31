package br.com.cecafes.controller;

import br.com.cecafes.model.Produto;
import br.com.cecafes.model.Produtor;
import br.com.cecafes.service.MessageService;
import br.com.cecafes.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
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

    @PostMapping(value = "/cadastrar")
    public String save(@ModelAttribute @Valid Produto produto) {
        produtoService.save(produto);
        return "redirect:/";
    }

    @GetMapping(value = "/form-produto")
    public String formProduto(Model model){
        model.addAttribute("produto", new Produto());

        return "formProduto";
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
                && (Objects.nonNull(produto.getNome()) || produto.getNome().isBlank()) && Objects.nonNull(produto.getQuantidade())
                && Objects.nonNull(produto.getUnidadeMedida()) && Objects.nonNull(produto.getPreco());

    }
}
