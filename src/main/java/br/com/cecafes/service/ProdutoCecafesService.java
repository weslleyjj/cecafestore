package br.com.cecafes.service;

import br.com.cecafes.model.Pedido;
import br.com.cecafes.model.ProdutoCecafes;
import br.com.cecafes.repository.ProdutoCecafesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
public class ProdutoCecafesService {

    private ProdutoCecafesRepository repository;

    @Autowired
    public ProdutoCecafesService(ProdutoCecafesRepository repository){
        this.repository = repository;
    }

    public List<ProdutoCecafes> findProdutosByIds(List<Long> listIds){
        return repository.findProdutosByIds(listIds);
    }
    public List<ProdutoCecafes> findAll() {
        return repository.findAll();
    }

    public ProdutoCecafes findById(Long id) {
        return repository.findById(id).get();
    }

    public ProdutoCecafes save(ProdutoCecafes pedido) {
        return repository.save(pedido);
    }

    public void deleteById(Long id) {
        ProdutoCecafes p = repository.findById(id).get();
        p.setAtivo(false);
        repository.save(p);
    }

    public Page<ProdutoCecafes> findPaginated(Pageable pageable) {
        List<ProdutoCecafes> produtos = repository.findAll();

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<ProdutoCecafes> list;

        if (produtos.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, produtos.size());
            list = produtos.subList(startItem, toIndex);
        }

        Page<ProdutoCecafes> pedidoPage = new PageImpl<>(list, PageRequest.of(currentPage, pageSize), produtos.size());

        return pedidoPage;
    }
}
