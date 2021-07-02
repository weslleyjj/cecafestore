package br.com.cecafes.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Comprador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String contato;
    private String cpf;
    @OneToOne
    private Endereco endereco;
    @OneToMany(mappedBy = "comprador")
    private List<Pedido> pedidos;
}
