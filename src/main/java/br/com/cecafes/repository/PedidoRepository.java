package br.com.cecafes.repository;

import br.com.cecafes.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Query(value = "SELECT * FROM pedido WHERE comprador_id = ?1 ORDER BY id DESC", nativeQuery = true)
    public List<Pedido> findPedidosByCompradorId(Long idComprador);

    @Query(value = "SELECT * FROM pedido WHERE status = 'PENDENTE' ORDER BY data_registro ASC", nativeQuery = true)
    public List<Pedido> findAllByPedidoPendente();

    public List<Pedido> findAllByNumero(String numero);
}
