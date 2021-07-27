package br.com.cecafes.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Audited
@Entity
public class Produtor extends Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @NotEmpty
    private String nome;
    @NotBlank
    @NotEmpty
    private String contato;
    @NotBlank
    @NotEmpty
    private String cpf;
    @OneToOne(cascade = CascadeType.ALL)
    private Endereco endereco;
    @OneToMany(mappedBy = "produtor")
    private List<Produto> produtos;
}
