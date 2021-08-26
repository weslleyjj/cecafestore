package br.com.cecafes.dto;

import br.com.cecafes.model.Produto;
import br.com.cecafes.model.ProdutoCecafes;
import br.com.cecafes.model.Produtor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProdutoDTO {
    private String nome;
    private String categoria;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dataValidade;
    private Integer quantidade;
    private String unidadeMedida;
    private Produtor produtor;
    private String preco;
    private MultipartFile foto;
    private String fotoUrl;

    public Produto getProduto(){
        Produto p = new Produto();
        p.setNome(nome);
        p.setCategoria(categoria);
        p.setDataValidade(dataValidade);
        p.setQuantidade(quantidade);
        p.setUnidadeMedida(unidadeMedida);
        p.setProdutor(produtor);
        p.setPreco(preco);
        p.setFotoUrl(fotoUrl);
        return p;
    }

    public ProdutoCecafes getProdutoCecafes(){
        ProdutoCecafes p = new ProdutoCecafes();
        p.setNome(nome);
        p.setCategoria(categoria);
        p.setQuantidade(quantidade);
        p.setUnidadeMedida(unidadeMedida);
        p.setPreco(preco);
        p.setFotoUrl(fotoUrl);
        return p;
    }
}
