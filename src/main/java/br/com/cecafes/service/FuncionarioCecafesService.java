package br.com.cecafes.service;

import br.com.cecafes.model.FuncionarioCecafes;
import br.com.cecafes.repository.FuncionarioCecafesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FuncionarioCecafesService {
    private FuncionarioCecafesRepository funcionarioCecafesRepository;

    @Autowired
    public FuncionarioCecafesService(FuncionarioCecafesRepository funcionarioCecafesRepository) {
        this.funcionarioCecafesRepository = funcionarioCecafesRepository;
    }

    public List<FuncionarioCecafes> findAll() {
        return funcionarioCecafesRepository.findAll();
    }

    public Optional<FuncionarioCecafes> findById(Long id) {
        return funcionarioCecafesRepository.findById(id);
    }

    public FuncionarioCecafes save(FuncionarioCecafes funcionarioCecafes) {
        return funcionarioCecafesRepository.save(funcionarioCecafes);
    }

    public void deleteById(Long id) {
        funcionarioCecafesRepository.deleteById(id);
    }
}
