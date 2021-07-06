package br.com.cecafes.service;

import br.com.cecafes.model.Produtor;
import br.com.cecafes.repository.ProdutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutorService {
    private ProdutorRepository produtorRepository;

    @Autowired
    public ProdutorService(ProdutorRepository produtorRepository) {
        this.produtorRepository = produtorRepository;
    }

    public List<Produtor> findAll() {
        return produtorRepository.findAll();
    }

    public Optional<Produtor> findById(Long id) {
        return produtorRepository.findById(id);
    }

    public Produtor save(Produtor produtor) {
        return produtorRepository.save(produtor);
    }

    public void deleteById(Long id) {
        produtorRepository.deleteById(id);
    }
}
