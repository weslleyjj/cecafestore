package br.com.cecafes.controller;

import br.com.cecafes.dto.ProdutoDTO;
import br.com.cecafes.model.Produto;
import br.com.cecafes.model.Produtor;
import br.com.cecafes.model.User;
import br.com.cecafes.repository.UserRepository;
import br.com.cecafes.security.MyUserDetails;
import br.com.cecafes.service.MessageService;
import br.com.cecafes.service.ProdutoService;
import br.com.cecafes.service.ProdutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.model.IModel;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/produto")
public class ProdutoController {
    private ProdutoService produtoService;
    private MessageService messageService;
    private ProdutorService produtorService;
    private UserRepository userRepository;

    @Value("${arquivos.produtor}")
    private String localDasImagens;

    @Autowired
    public ProdutoController(ProdutoService produtoService, MessageService messageService, ProdutorService produtorService, UserRepository userRepository) {
        this.produtoService = produtoService;
        this.messageService = messageService;
        this.produtorService = produtorService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Produto> findAll() {
        return produtoService.findAll();
    }

    @PostMapping(value = "/cadastrar")
    public String save(@ModelAttribute @Valid ProdutoDTO produto) {
        Produtor produtor = getProdutor();

        produto.setProdutor(produtor);

        produto.setFotoUrl(uploadFoto(produto.getFoto(), produto.getNome(), produto.getProdutor().getId()));
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

    @RequestMapping(value = "/editar/{id}")
    public String editar(@PathVariable(name = "id") Long id, Model model) {
        Produto produtoUpdate = produtoService.findById(id);
        ProdutoDTO produtoDTO = new ProdutoDTO();
        produtoDTO = produtoDTO.getProdutoDTO(produtoUpdate);
        model.addAttribute("produto", produtoDTO);

        return "formEditarProduto";
    }

    @PostMapping(value = "/saveEditar")
    public String saveEditar(@ModelAttribute @Valid ProdutoDTO produto) {
        Produtor produtor = getProdutor();

        produto.setProdutor(produtor);

        produto.setFotoUrl(uploadFoto(produto.getFoto(), produto.getNome(), produto.getProdutor().getId()));
        produtoService.save(produto.getProdutoEditado());

        return "redirect:/produtor/listar";
    }

    @RequestMapping(value = "/deletar/{id}")
    public String delete(@PathVariable(name = "id") Long id) {
        produtoService.deleteById(id);

        return "redirect:/produtor/listar";
    }

    private boolean validaProduto(Produto produto){
        return (Objects.nonNull(produto.getCategoria()) || produto.getCategoria().isBlank()) && Objects.nonNull(produto.getDataValidade())
                && (Objects.nonNull(produto.getNome()) || produto.getNome().isBlank()) && Objects.nonNull(produto.getQuantidade())
                && Objects.nonNull(produto.getUnidadeMedida()) && Objects.nonNull(produto.getPreco());

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

    private Produtor getProdutor(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        User user = userRepository.getUserByUsername(userDetails.getUsername());
        Produtor produtor = produtorService.findByUsername(user.getUsername());

        return produtor;
    }
}
