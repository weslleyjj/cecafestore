package br.com.cecafes.controller;

import br.com.cecafes.dto.ProdutoDTO;
import br.com.cecafes.model.Produto;
import br.com.cecafes.model.Produtor;
import br.com.cecafes.service.MessageService;
import br.com.cecafes.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/produto")
public class ProdutoController {
    private ProdutoService produtoService;
    private MessageService messageService;

    @Value("${arquivos.imagens}")
    private String localDasImagens;

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
    public String save(@ModelAttribute @Valid ProdutoDTO produto) {
        produto.setFotoUrl(uploadFoto(produto.getFoto(), 1L));
        produtoService.save(produto.getProduto());
        return "redirect:/";
    }

    @GetMapping(value = "/form-produto")
    public String formProduto(Model model){
        model.addAttribute("produto", new ProdutoDTO());

        return "formProduto";
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Produto produto = produtoService.findById(id);
        if (Objects.isNull(produto)) {
            return ResponseEntity.status(404).body(messageService.createJson("message", "Produto não encontrado"));
        } else {
            return ResponseEntity.ok(produto);
        }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> update(@RequestBody Produto produto) {
        Produto produtoUpdate = produtoService.findById(produto.getId());

        if (Objects.isNull(produtoUpdate)) {
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
        Produto produtoDelete = produtoService.findById(id);

        if (Objects.isNull(produtoDelete)) {
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

    private String uploadFoto(MultipartFile file, Long id){
        if (file.isEmpty()) {
            return "";
        }
        try {

            byte[] bytes = file.getBytes();
            String nomeDaImagem = id+"-" + file.getOriginalFilename();
            Path path = Paths.get(localDasImagens + nomeDaImagem);
            Files.write(path, bytes);

            return nomeDaImagem;

        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
