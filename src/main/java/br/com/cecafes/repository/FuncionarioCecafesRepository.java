package br.com.cecafes.repository;

import br.com.cecafes.model.FuncionarioCecafes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuncionarioCecafesRepository extends JpaRepository<FuncionarioCecafes, Long> {
}
