package br.com.cecafes.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    @NotBlank(message = "Rua não pode estar em branco")
    @Size(min = 5, message = "Rua deve ter pelo menos 5 caracteres")
    private String rua;

    @NotNull
    @NotEmpty
    @NotBlank(message = "Número não pode estar em branco")
    private String numero;

    @NotNull
    @NotEmpty
    @NotBlank(message = "Bairro não pode estar em branco")
    @Size(min = 5, message = "Bairro deve ter pelo menos 5 caracteres")
    private String bairro;

    @NotNull
    @NotEmpty
    @NotBlank(message = "Cidade não pode estar em branco")
    @Size(min = 5, message = "Cidade deve ter pelo menos 5 caracteres")
    private String cidade;

    @NotNull
    @NotEmpty
    @NotBlank(message = "CEP não pode estar em branco")
    @Size(min = 5, message = "CEP deve ter pelo menos 5 caracteres")
    private String cep;

    @NotNull
    @NotEmpty
    @NotBlank(message = "Estado não pode estar em branco")
    @Size(min = 5, message = "Estado deve ter pelo menos 5 caracteres")
    private String estado;

    private String complemento;
}
