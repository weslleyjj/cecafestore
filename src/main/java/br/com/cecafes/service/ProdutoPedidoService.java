package br.com.cecafes.service;

import br.com.cecafes.model.ProdutoPedido;
import br.com.cecafes.repository.ProdutoPedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoPedidoService {

    private ProdutoPedidoRepository repository;

    @Autowired
    public ProdutoPedidoService(ProdutoPedidoRepository repository){
        this.repository = repository;
    }

    public List<ProdutoPedido> findAll() {
        return repository.findAll();
    }

    public Optional<ProdutoPedido> findById(Long id) {
        return repository.findById(id);
    }

    public ProdutoPedido save(ProdutoPedido pedido) {
        return repository.save(pedido);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
