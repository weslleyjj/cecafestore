package br.com.cecafes.controller;

import br.com.cecafes.model.Comprador;
import br.com.cecafes.model.ProdutoCecafes;
import br.com.cecafes.model.User;
import br.com.cecafes.repository.UserRepository;
import br.com.cecafes.security.MyUserDetails;
import br.com.cecafes.service.CompradorService;
import br.com.cecafes.service.ProdutoCecafesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping(value = "/loja")
public class LojaController {
    private ProdutoCecafesService produtoCecafesService;
    private CompradorService compradorService;
    private UserRepository userRepository;

    @Autowired
    public LojaController(
            ProdutoCecafesService produtoCecafesService,
            CompradorService compradorService,
            UserRepository userRepository
    ) {
        this.produtoCecafesService = produtoCecafesService;
        this.compradorService = compradorService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String homeLoja(
            Model model,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size
    ) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(9);

        Page<ProdutoCecafes> produtoPage;

        List<ProdutoCecafes> produtos = produtoCecafesService.findAll();
        produtoPage = produtoCecafesService.findPaginated(PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("produtos", produtos);
        model.addAttribute("produtoPage", produtoPage);
        int totalPages = produtoPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "shop-grid";
    }
}
