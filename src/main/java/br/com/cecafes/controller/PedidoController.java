package br.com.cecafes.controller;

import br.com.cecafes.model.Pedido;
import br.com.cecafes.service.MessageService;
import br.com.cecafes.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/pedido")
public class PedidoController {
    private PedidoService pedidoService;
    private MessageService messageService;

    @Autowired
    public PedidoController(PedidoService pedidoService, MessageService messageService) {
        this.pedidoService = pedidoService;
        this.messageService = messageService;
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
}
