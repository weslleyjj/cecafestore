package br.com.cecafes.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Audited
@Entity
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @NotEmpty
    private String nome;
    @NotNull
    @NotEmpty
    private String categoria;
    @NotNull
    private Date dataValidade;
    @NotNull
    private Integer qtdCaixa;
    @ManyToOne
    private Produtor produtor;
}
