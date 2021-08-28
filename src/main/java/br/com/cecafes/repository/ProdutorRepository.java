package br.com.cecafes.repository;

import br.com.cecafes.model.Produtor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutorRepository extends JpaRepository<Produtor, Long> {
    @Query(value = "SELECT c.* FROM produtor c join users u on c.user_id = u.id WHERE u.username = ?1", nativeQuery = true)
    public Produtor getProdutorByUsername(String username);
}
