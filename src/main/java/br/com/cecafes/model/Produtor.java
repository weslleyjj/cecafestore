package br.com.cecafes.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Produtor {
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
    @Valid
    private Endereco endereco;

    @OneToMany(mappedBy = "produtor")
    private List<Produto> produtos;

    @OneToOne(cascade = CascadeType.ALL)
    private User user;
}
