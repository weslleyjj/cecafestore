package br.com.cecafes.dto;

import br.com.cecafes.model.Comprador;
import br.com.cecafes.model.Endereco;
import br.com.cecafes.model.Pedido;
import br.com.cecafes.model.ProdutoPedido;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoListagemDTO {

    private Long id;
    private String numero;
    private Comprador comprador = new Comprador();
    private Endereco endereco;
    private String valorPedido;

    public PedidoListagemDTO(Pedido pedido){
        this.id = pedido.getId();
        this.numero = pedido.getNumero();
        this.comprador.setNome(pedido.getComprador().getNome());
        this.comprador.setContato(pedido.getComprador().getContato());
        this.endereco = pedido.getEndereco();
        this.valorPedido = pedido.getValorPedido();
    }
}
