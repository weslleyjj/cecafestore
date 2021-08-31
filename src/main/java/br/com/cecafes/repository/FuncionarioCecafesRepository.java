package br.com.cecafes.repository;

import br.com.cecafes.model.FuncionarioCecafes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FuncionarioCecafesRepository extends JpaRepository<FuncionarioCecafes, Long> {
    @Query(value = "SELECT c.* FROM funcionario_cecafes c join users u on c.user_id = u.id WHERE u.username = ?1", nativeQuery = true)
    public FuncionarioCecafes getFuncionarioCecafesByUsername(String username);
}
