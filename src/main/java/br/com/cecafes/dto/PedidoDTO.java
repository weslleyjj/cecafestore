package br.com.cecafes.dto;

import br.com.cecafes.model.Comprador;
import br.com.cecafes.model.ProdutoCecafes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDTO {
    @NotNull
    private Comprador comprador;

    @NotBlank
    private String numero;

    @NotEmpty
    private List<ProdutoCecafes> produtos;
}
