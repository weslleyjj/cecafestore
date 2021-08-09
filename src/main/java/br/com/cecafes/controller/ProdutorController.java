package br.com.cecafes.controller;

import br.com.cecafes.model.Produtor;
import br.com.cecafes.model.Role;
import br.com.cecafes.model.User;
import br.com.cecafes.repository.UserRepository;
import br.com.cecafes.service.EnderecoService;
import br.com.cecafes.service.MessageService;
import br.com.cecafes.service.ProdutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/produtor")
public class ProdutorController {
    private ProdutorService produtorService;
    private EnderecoService enderecoService;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private MessageService messageService;

    @Autowired
    public ProdutorController(ProdutorService produtorService, EnderecoService enderecoService, MessageService messageService, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.produtorService = produtorService;
        this.enderecoService = enderecoService;
        this.messageService = messageService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Produtor> findAll() {
        return produtorService.findAll();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<Produtor> produtorOptional = produtorService.findById(id);
        if (!produtorOptional.isPresent()) {
            return ResponseEntity.status(404).body(messageService.createJson("message", "Produtor não encontrado"));
        } else {https://igorescobar.github.io/jQuery-Mask-Plugin/
            return ResponseEntity.ok(produtorOptional.get());
        }
    }

    @PostMapping(value = "/cadastrar")
    public String save(@ModelAttribute @Valid Produtor produtor, Errors errors) {
        if(!errors.hasErrors()){
            produtor.getUser().setPassword(passwordEncoder.encode(produtor.getUser().getPassword()));

            // Definindo a Role do produtor no cadastro do mesmo
            Set<Role> papel = new HashSet<>();
            papel.add(new Role().builder().id(1).name("PRODUTOR").build());
            produtor.getUser().setRoles(papel);
            produtor.getUser().setEnabled(true);

            userRepository.save(produtor.getUser());
            produtorService.save(produtor);
            return "redirect:/";
        }

        return "formProdutor";
    }

    @GetMapping(value = "/form-produtor")
    public String formProdutor(Model model){
        model.addAttribute("produtor", new Produtor());

        return "formProdutor";
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> update(@RequestBody Produtor produtor) {
        Optional<Produtor> produtorOptional = produtorService.findById(produtor.getId());

        if (!produtorOptional.isPresent()) {
            return ResponseEntity.status(404).body(messageService.createJson("message", "Produtor não encontrado"));
        } else {
            produtor.getUser().setPassword(passwordEncoder.encode(produtor.getUser().getPassword()));
            return ResponseEntity.ok(produtorService.save(produtor));
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Produtor> produtorOptional = produtorService.findById(id);

        if (!produtorOptional.isPresent()) {
            return ResponseEntity.status(404).body(messageService.createJson("message", "Produtor não encontrado"));
        } else {
            enderecoService.deleteById(produtorOptional.get().getEndereco().getId());
            produtorService.deleteById(id);
            return ResponseEntity.status(204).build();
        }
    }
}
