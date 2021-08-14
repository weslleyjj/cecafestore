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

    public ProdutoPedido findById(Long id) {
        return repository.findById(id).get();
    }

    public ProdutoPedido save(ProdutoPedido produtoPedido) {
        return repository.save(produtoPedido);
    }

    public List<ProdutoPedido> saveAll(List<ProdutoPedido> produtos) {
        return repository.saveAll(produtos);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
