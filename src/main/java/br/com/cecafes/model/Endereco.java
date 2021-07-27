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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Audited
@Entity
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @NotEmpty
    private String rua;
    @NotBlank
    @NotEmpty
    private String numero;
    @NotNull
    @NotEmpty
    private String bairro;
    @NotBlank
    @NotEmpty
    private String cidade;
    @NotBlank
    @NotEmpty
    private String cep;
    @NotBlank
    @NotEmpty
    private String estado;
    private String complemento;
}
