package br.com.cecafes.repository;

import br.com.cecafes.model.ProdutoCecafes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoCecafesRepository extends JpaRepository<ProdutoCecafes, Long> {

    @Query(value = "SELECT * FROM produto_cecafes WHERE id in ?1", nativeQuery = true)
    List<ProdutoCecafes> findProdutosByIds(List<Long> listId);

}
