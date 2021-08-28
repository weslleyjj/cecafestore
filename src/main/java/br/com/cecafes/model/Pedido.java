package br.com.cecafes.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    private String numero;
    @ManyToOne
    @JoinColumn(name = "comprador_id")
    private Comprador comprador;
    @OneToOne
    private Endereco endereco;
    @OneToMany
    private List<ProdutoPedido> produtosPedido;

    private String status;

    private Float valorPedido;
}
