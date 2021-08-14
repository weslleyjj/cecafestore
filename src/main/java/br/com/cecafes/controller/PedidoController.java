package br.com.cecafes.controller;

import br.com.cecafes.model.Comprador;
import br.com.cecafes.model.Pedido;
import br.com.cecafes.model.Produto;
import br.com.cecafes.service.CompradorService;
import br.com.cecafes.service.MessageService;
import br.com.cecafes.service.PedidoService;
import br.com.cecafes.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/pedido")
public class PedidoController {
    private PedidoService pedidoService;
    private MessageService messageService;
    private ProdutoService produtoService;
    private CompradorService compradorService;

    @Autowired
    public PedidoController(PedidoService pedidoService, MessageService messageService, ProdutoService produtoService, CompradorService compradorService) {
        this.pedidoService = pedidoService;
        this.messageService = messageService;
        this.produtoService = produtoService;
        this.compradorService = compradorService;
    }

    @GetMapping
    public List<Pedido> findAll() {
        return pedidoService.findAll();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<Pedido> pedidoOptional = pedidoService.findById(id);
        if (!pedidoOptional.isPresent()) {
            return ResponseEntity.status(404).body(messageService.createJson("message", "Pedido não encontrado"));
        } else {
            return ResponseEntity.ok(pedidoOptional.get());
        }
    }

    @PostMapping
    public ModelAndView save(@ModelAttribute @Valid Pedido pedido) {

        //laço para resgatar os produtos e subistituir na lista
        for(int i = 0; i < pedido.getProdutos().size() ; i++){
            Produto produtoTmp = produtoService.findById(pedido.getProdutos().get(i).getId());
            produtoTmp.setQuantidade(pedido.getProdutos().get(i).getQuantidade());

            if (!Objects.isNull(produtoTmp)) {
                pedido.getProdutos().remove(i);
                pedido.getProdutos().add(i, produtoTmp);
            }
        }

        // TODO: ajustar essa linha posteriormente para nao permitir o ADMIN acessar essa rota
        Comprador comprador = compradorService.findById(pedido.getComprador().getId());

        pedido.setEndereco(comprador.getEndereco());
        pedido.setValorPedido(calculaPedido(pedido.getProdutos()));

        pedidoService.save(pedido);
        return new ModelAndView("index");
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> update(@RequestBody Pedido pedido) {
        Optional<Pedido> pedidoOptional = pedidoService.findById(pedido.getId());

        if (!pedidoOptional.isPresent()) {
            return ResponseEntity.status(404).body(messageService.createJson("message", "Pedido não encontrado"));
        } else {
            return ResponseEntity.ok(pedidoService.save(pedido));
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Pedido> pedidoOptional = pedidoService.findById(id);

        if (!pedidoOptional.isPresent()) {
            return ResponseEntity.status(404).body(messageService.createJson("message", "Pedido não encontrado"));
        } else {
            pedidoService.deleteById(id);
            return ResponseEntity.status(204).build();
        }
    }

    private Float calculaPedido(List<Produto> produtos){
        Float valorTotal = 0f;

        for (Produto produto : produtos) {
            valorTotal += Float.parseFloat(produto.getPreco().replace(',', '.')) * produto.getQuantidade();
        }

        return valorTotal;
    }
}
