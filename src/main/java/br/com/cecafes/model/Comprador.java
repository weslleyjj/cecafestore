package br.com.cecafes.model;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Comprador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    @NotBlank(message = "Nome não pode estar em branco")
    @Size(min = 10, message = "Nome deve ter pelo menos 10 caracteres")
    private String nome;

    @NotNull
    @NotEmpty
    @NotBlank(message = "Contato não pode estar em branco")
    @Size(min = 16, max = 16, message = "Contato inválido")
    private String contato;

    @NotNull
    @NotEmpty
    @NotBlank(message = "CPF não pode estar em branco")
    @Size(min = 14, max = 14, message = "CPF inválido")
    private String cpf;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="endereco_id")
    @Valid
    private Endereco endereco;

    @OneToMany(mappedBy = "comprador")
    private List<Pedido> pedidos;

    @OneToOne(cascade = CascadeType.ALL)
    private User user;
}
