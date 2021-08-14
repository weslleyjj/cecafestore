package br.com.cecafes.service;

import br.com.cecafes.model.Comprador;
import br.com.cecafes.repository.CompradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompradorService {
    private CompradorRepository compradorRepository;

    @Autowired
    public CompradorService(CompradorRepository compradorRepository) {
        this.compradorRepository = compradorRepository;
    }

    public List<Comprador> findAll() {
        return compradorRepository.findAll();
    }

    public Comprador findById(Long id) {
        return compradorRepository.findById(id).get();
    }

    public Comprador findByUsername(String username){
        return compradorRepository.getCompradorByUsername(username);
    }

    public Comprador save(Comprador comprador) {
        return compradorRepository.save(comprador);
    }

    public void deleteById(Long id) {
        compradorRepository.deleteById(id);
    }
}
