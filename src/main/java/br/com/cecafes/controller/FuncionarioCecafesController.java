package br.com.cecafes.controller;

import br.com.cecafes.model.FuncionarioCecafes;
import br.com.cecafes.model.Role;
import br.com.cecafes.repository.UserRepository;
import br.com.cecafes.service.FuncionarioCecafesService;
import br.com.cecafes.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/funcionario")
public class FuncionarioCecafesController {
    private FuncionarioCecafesService funcionarioCecafesService;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private MessageService messageService;

    @Autowired
    public FuncionarioCecafesController(FuncionarioCecafesService funcionarioCecafesService, MessageService messageService, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.funcionarioCecafesService = funcionarioCecafesService;
        this.messageService = messageService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<FuncionarioCecafes> findAll() {
        return funcionarioCecafesService.findAll();
    }

    @GetMapping("/gerencia-loja")
    public ModelAndView paginaGerencia(){
        return new ModelAndView("gerenciaLoja");
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

    @PostMapping(value = "/cadastrar")
    public String save(@ModelAttribute @Valid FuncionarioCecafes funcionarioCecafes) {
        funcionarioCecafes.getUser().setPassword(passwordEncoder.encode(funcionarioCecafes.getUser().getPassword()));

        // Definindo a Role do funcionario no cadastro do mesmo
        Set<Role> papel = new HashSet<>();
        papel.add(new Role().builder().id(3).name("FUNCIONARIO").build());
        funcionarioCecafes.getUser().setRoles(papel);
        funcionarioCecafes.getUser().setEnabled(true);

        userRepository.save(funcionarioCecafes.getUser());
        funcionarioCecafesService.save(funcionarioCecafes);
        return "redirect:/";
    }

    @GetMapping(value = "/form-funcionario")
    public String formFuncionarioCecafes(Model model) {
        model.addAttribute("funcionario", new FuncionarioCecafes());
        return "form-funcionario-cecafes";
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> update(@RequestBody FuncionarioCecafes funcionarioCecafes) {
        Optional<FuncionarioCecafes> funcionarioCecafesOptional = funcionarioCecafesService.findById(funcionarioCecafes.getId());

        if (!funcionarioCecafesOptional.isPresent()) {
            return ResponseEntity.status(404).body(messageService.createJson("message", "FuncionarioCecafes não encontrado"));
        } else {
            funcionarioCecafes.getUser().setPassword(passwordEncoder.encode(funcionarioCecafes.getUser().getPassword()));
            return ResponseEntity.ok(funcionarioCecafesService.save(funcionarioCecafes));
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
