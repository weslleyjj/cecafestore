package br.com.cecafes.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String numero;
    @ManyToOne
    @JoinColumn(name = "comprador_id")
    private Comprador comprador;
    @OneToOne
    private Endereco endereco;
    @OneToMany
    private List<Produto> produto;
}
