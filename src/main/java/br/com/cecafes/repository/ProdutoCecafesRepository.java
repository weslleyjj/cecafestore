package br.com.cecafes.repository;

import br.com.cecafes.model.ProdutoCecafes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoCecafesRepository extends JpaRepository<ProdutoCecafes, Long> {
}
