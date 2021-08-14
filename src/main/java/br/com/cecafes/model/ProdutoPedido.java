package br.com.cecafes.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ProdutoPedido{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @OneToOne
    private ProdutoCecafes produtoCecafes;
    @NotEmpty
    private String nome;
    @NotEmpty
    private String categoria;
    @NotNull
    private Integer quantidade;
    @NotBlank
    private String unidadeMedida;
    @NotNull
    private String preco;
    @ManyToOne(cascade = CascadeType.ALL)
    private Pedido pedido;

    public ProdutoPedido(ProdutoCecafes produto){
        this.produtoCecafes = produto;
        this.nome = produto.getNome();
        this.categoria = produto.getCategoria();
        this.quantidade = produto.getQuantidade();
        this.unidadeMedida = produto.getUnidadeMedida();
        this.preco = produto.getPreco();
    }
}
