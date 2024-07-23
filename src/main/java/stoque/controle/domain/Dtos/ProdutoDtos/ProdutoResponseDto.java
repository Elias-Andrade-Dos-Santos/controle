package stoque.controle.domain.Dtos.ProdutoDtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import stoque.controle.domain.Enum.TipoAlimento;

public record ProdutoResponseDto(String nome, String descricao, BigDecimal preco, int quantidadeEmEstoque, LocalDate dataDeValidade, TipoAlimento tipoAlimento) {
    
}
