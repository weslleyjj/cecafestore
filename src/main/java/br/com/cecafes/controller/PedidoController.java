package br.com.cecafes.controller;

import br.com.cecafes.dto.PedidoDTO;
import br.com.cecafes.dto.PedidoListagemDTO;
import br.com.cecafes.model.*;
import br.com.cecafes.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/pedido")
public class PedidoController {
    private PedidoService pedidoService;
    private MessageService messageService;
    private ProdutoCecafesService produtoCecafesService;
    private CompradorService compradorService;
    private ProdutoPedidoService produtoPedidoService;

    @Autowired
    public PedidoController(PedidoService pedidoService, MessageService messageService, ProdutoCecafesService produtoCecafesService, CompradorService compradorService, ProdutoPedidoService produtoPedidoService) {
        this.pedidoService = pedidoService;
        this.messageService = messageService;
        this.produtoCecafesService = produtoCecafesService;
        this.compradorService = compradorService;
        this.produtoPedidoService = produtoPedidoService;
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
    public String save(@ModelAttribute @Valid PedidoDTO pedido) {
        List<ProdutoPedido> produtoPedidoList = new ArrayList<>();
        Pedido pedidoOficial = new Pedido();

        List<Long> idsProdutos = new ArrayList<>();
        List<ProdutoCecafes> produtosAnteriores = pedido.getProdutos();

        pedido.getProdutos().forEach(produto -> {
            idsProdutos.add(produto.getId());
        });

        pedido.setProdutos(produtoCecafesService.findProdutosByIds(idsProdutos));

        for(int i = 0; i < pedido.getProdutos().size() ; i++){

                Long idTmp = produtosAnteriores.get(i).getId();
                int qtd = produtosAnteriores.get(i).getQuantidade();

                if(pedido.getProdutos().stream().anyMatch(pId -> Objects.equals(pId.getId(), idTmp))){
                    for (ProdutoCecafes produto : pedido.getProdutos()) {
                        if(Objects.equals(produto.getId(), idTmp)){
                            pedido.getProdutos().get(i).setQuantidade(qtd);
                        }
                    }
                }

                if(pedido.getProdutos().stream().anyMatch(pId -> pId.getId() == idTmp)){
                    pedido.getProdutos().get(i).setQuantidade(qtd);
                }

                produtoPedidoList.add(new ProdutoPedido(pedido.getProdutos().get(i)));
        }

        Comprador comprador = compradorService.findById(pedido.getComprador().getId());

        // Caso seja admin ou outra role, nao ira cumprir a operaçao
        if(Objects.isNull(comprador)){
            return "produtosListComprador";
        }

        pedidoOficial.setComprador(comprador);
        pedidoOficial.setEndereco(comprador.getEndereco());
        pedidoOficial.setValorPedido(calculaPedido(produtoPedidoList));
        pedidoOficial.setNumero(pedido.getNumero());


        pedidoService.save(pedidoOficial);

        produtoPedidoList.forEach(produtoPedido -> produtoPedido.setPedido(pedidoOficial));

        produtoPedidoService.saveAll(produtoPedidoList);

        pedidoOficial.setProdutosPedido(produtoPedidoList);

        pedidoService.save(pedidoOficial);

        return "index";
    }

    @GetMapping("/gerencia-pedidos")
    public ModelAndView listagemPedidos(){
        ModelAndView model = new ModelAndView("listagemPedidosCecafes");
        List<PedidoListagemDTO> pedidoListagem = new ArrayList<>();
        List<Pedido> pedidoList = pedidoService.findAll();

        pedidoList.forEach(pedido -> {
            pedidoListagem.add(new PedidoListagemDTO(pedido));
        });

        model.addObject("pedidos", pedidoListagem);

        return model;
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

    private Float calculaPedido(List<ProdutoPedido> produtos){
        Float valorTotal = 0f;

        for (ProdutoPedido produto : produtos) {
            valorTotal += Float.parseFloat(produto.getPreco().replace(',', '.')) * produto.getQuantidade();
        }

        return valorTotal;
    }
}
