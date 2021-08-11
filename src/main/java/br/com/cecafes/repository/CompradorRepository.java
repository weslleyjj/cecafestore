package br.com.cecafes.repository;

import br.com.cecafes.model.Comprador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CompradorRepository extends JpaRepository<Comprador, Long> {

    @Query(value = "SELECT c.* FROM comprador c join users u on c.user_user_id = u.user_id WHERE u.username = ?1", nativeQuery = true)
    public Comprador getCompradorByUsername(String username);
}
