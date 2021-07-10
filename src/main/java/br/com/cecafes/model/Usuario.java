package br.com.cecafes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class Usuario {
    @NotNull
    @NotEmpty
    private String login;
    @NotNull
    @NotEmpty
    private String senha;
    @JsonIgnore
    private String codigoTrocaSenha;
    @JsonIgnore
    private LocalDateTime codigoValidade;
}
