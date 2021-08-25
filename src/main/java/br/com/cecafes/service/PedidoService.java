package br.com.cecafes.service;

import br.com.cecafes.model.Pedido;
import br.com.cecafes.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {
    private PedidoRepository pedidoRepository;

    @Autowired
    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> findById(Long id) {
        return pedidoRepository.findById(id);
    }

    public Pedido save(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    public List<Pedido> findPedidosByCompradorId(Long idComprador){
        return pedidoRepository.findPedidosByCompradorId(idComprador);
    }

    public void deleteById(Long id) {
        pedidoRepository.deleteById(id);
    }
}
