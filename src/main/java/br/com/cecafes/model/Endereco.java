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
@Audited
@Entity
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    @NotBlank
    private String rua;

    @NotNull
    @NotEmpty
    @NotBlank
    private String numero;

    @NotNull
    @NotEmpty
    @NotBlank
    private String bairro;

    @NotNull
    @NotEmpty
    @NotBlank
    private String cidade;

    @NotNull
    @NotEmpty
    @NotBlank
    private String cep;

    @NotNull
    @NotEmpty
    @NotBlank
    private String estado;

    private String complemento;
}
