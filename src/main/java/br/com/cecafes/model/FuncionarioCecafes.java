package br.com.cecafes.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FuncionarioCecafes {
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
    @NotBlank(message = "Matrícula não pode estar em branco")
    @Size(min = 5, message = "Matrícula deve ter pelo menos 5 caracteres")
    private String matricula;

    @NotNull
    @NotEmpty
    @NotBlank(message = "CPF não pode estar em branco")
    @Size(min = 14, max = 14, message = "CPF inválido")
    private String cpf;

    @OneToOne(cascade = CascadeType.ALL)
    private User user;
}
