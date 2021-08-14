package br.com.cecafes.service;

import br.com.cecafes.model.ProdutoCecafes;
import br.com.cecafes.repository.ProdutoCecafesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProdutoCecafesService {

    private ProdutoCecafesRepository repository;

    @Autowired
    public ProdutoCecafesService(ProdutoCecafesRepository repository){
        this.repository = repository;
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
        repository.deleteById(id);
    }
}
