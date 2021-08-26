package br.com.cecafes.controller;

import br.com.cecafes.dto.ProdutoDTO;
import br.com.cecafes.model.ProdutoCecafes;
import br.com.cecafes.service.MessageService;
import br.com.cecafes.service.ProdutoCecafesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/produtoCecafes")
public class ProdutoCecafesController {
    private ProdutoCecafesService produtoCecafesService;
    private MessageService messageService;

    @Value("${arquivos.imagens}")
    private String localDasImagens;

    @Autowired
    public ProdutoCecafesController(ProdutoCecafesService produtoCecafesService, MessageService messageService) {
        this.produtoCecafesService = produtoCecafesService;
        this.messageService = messageService;
    }

    @GetMapping
    public List<ProdutoCecafes> findAll() {
        return produtoCecafesService.findAll();
    }

    @PostMapping(value = "/cadastrar")
    public String save(@ModelAttribute @Valid ProdutoDTO produto) {
        produto.setFotoUrl(uploadFoto(produto.getFoto(), 1L));
        produtoCecafesService.save(produto.getProdutoCecafes());
        return "redirect:/";
    }

    @GetMapping(value = "/form-produto-cecafes")
    public String formProdutoCecafes(Model model){
        model.addAttribute("produtoCecafes", new ProdutoDTO());

        return "formProdutoCecafes";
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        ProdutoCecafes produto = produtoCecafesService.findById(id);
        if (Objects.isNull(produto)) {
            return ResponseEntity.status(404).body(messageService.createJson("message", "Produto não encontrado"));
        } else {
            return ResponseEntity.ok(produto);
        }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> update(@RequestBody ProdutoCecafes produto) {
        ProdutoCecafes produtoUpdate = produtoCecafesService.findById(produto.getId());

        if (Objects.isNull(produtoUpdate)) {
            return ResponseEntity.status(404).body(messageService.createJson("message", "Produto não encontrado"));
        } else {
            return ResponseEntity.ok(produtoCecafesService.save(produto));
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        ProdutoCecafes produtoDelete = produtoCecafesService.findById(id);

        if (Objects.isNull(produtoDelete)) {
            return ResponseEntity.status(404).body(messageService.createJson("message", "Produto não encontrado"));
        } else {
            produtoCecafesService.deleteById(id);
            return ResponseEntity.status(204).build();
        }
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
