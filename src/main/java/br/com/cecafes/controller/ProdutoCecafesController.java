package br.com.cecafes.controller;

import br.com.cecafes.dto.ProdutoDTO;
import br.com.cecafes.model.FuncionarioCecafes;
import br.com.cecafes.model.ProdutoCecafes;
import br.com.cecafes.model.Produtor;
import br.com.cecafes.model.User;
import br.com.cecafes.repository.UserRepository;
import br.com.cecafes.security.MyUserDetails;
import br.com.cecafes.service.FuncionarioCecafesService;
import br.com.cecafes.service.MessageService;
import br.com.cecafes.service.ProdutoCecafesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/produtoCecafes")
public class ProdutoCecafesController {
    private UserRepository userRepository;
    private FuncionarioCecafesService funcionarioCecafesService;
    private ProdutoCecafesService produtoCecafesService;
    private MessageService messageService;

    @Value("${arquivos.cecafes}")
    private String localDasImagens;

    @Autowired
    public ProdutoCecafesController(
            UserRepository userRepository,
            FuncionarioCecafesService funcionarioCecafesService,
            ProdutoCecafesService produtoCecafesService,
            MessageService messageService
    ) {
        this.userRepository = userRepository;
        this.funcionarioCecafesService = funcionarioCecafesService;
        this.produtoCecafesService = produtoCecafesService;
        this.messageService = messageService;
    }

    @GetMapping
    public List<ProdutoCecafes> findAll() {
        return produtoCecafesService.findAll();
    }

    @PostMapping(value = "/cadastrar")
    public String save(@ModelAttribute @Valid ProdutoDTO produto) {
        FuncionarioCecafes funcionarioCecafes = getFuncionario();
        produto.setFotoUrl(uploadFoto(produto.getFoto(), produto.getNome(), funcionarioCecafes.getId()));
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

    private String uploadFoto(MultipartFile file, String nomeAppend, Long id){
        if (file.isEmpty()) {
            return "";
        }
        try {
            String fileName = id.toString() + "-" + nomeAppend + "-" + file.getOriginalFilename();

            Path path = Paths.get(localDasImagens).toAbsolutePath().normalize();
            Files.createDirectories(path);

            Path targetLocation = path.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;

        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private FuncionarioCecafes getFuncionario(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        User user = userRepository.getUserByUsername(userDetails.getUsername());
        FuncionarioCecafes funcionarioCecafes = funcionarioCecafesService.findByUsername(user.getUsername());

        return funcionarioCecafes;
    }
}
