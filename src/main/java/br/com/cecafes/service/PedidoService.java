package br.com.cecafes.service;

import br.com.cecafes.model.Pedido;
import br.com.cecafes.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
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

    public Page<Pedido> findPaginated(Pageable pageable, @Nullable Long idComprador) {
        List<Pedido> pedidos;
        if(Objects.isNull(idComprador)){
            pedidos = pedidoRepository.findAll();
        }else{
            pedidos = pedidoRepository.findPedidosByCompradorId(idComprador);
        }

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Pedido> list;

        if (pedidos.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, pedidos.size());
            list = pedidos.subList(startItem, toIndex);
        }

        Page<Pedido> pedidoPage
                = new PageImpl<Pedido>(list, PageRequest.of(currentPage, pageSize), pedidos.size());

        return pedidoPage;
    }
}
