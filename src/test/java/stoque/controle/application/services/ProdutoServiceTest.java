package stoque.controle.application.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import stoque.controle.domain.Dtos.ProdutoDtos.ProdutoRequestDto;
import stoque.controle.domain.Enum.TipoAlimento;
import stoque.controle.domain.entitys.produtoEntity.Produto;
import stoque.controle.domain.exceptions.ProdutoAlreadyExistsException;
import stoque.controle.domain.exceptions.ProdutoNotFoundException;
import stoque.controle.domain.repositories.ProdutoRepository;

@ExtendWith(MockitoExtension.class)
public class ProdutoServiceTest {
    @Mock
    private ProdutoRepository produtoRepository;
    
    @InjectMocks
    private ProdutoService produtoService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void testCreateProdutoSuccess() {
        ProdutoRequestDto produtoRequestDto = new ProdutoRequestDto("Produto Teste", "Descrição Teste", BigDecimal.ONE, 1, LocalDate.now(), TipoAlimento.CEREAIS);
        Produto produto = new Produto();
        produto.setNome("Produto Teste");
        produto.setDescricao("Descrição Teste");
        produto.setPreco(BigDecimal.ONE);
        produto.setDataDeValidade(LocalDate.now());
        produto.setQuantidadeEmEstoque(10);
        produto.setTipoAlimento(TipoAlimento.CEREAIS);

        when(produtoRepository.findByNome(produtoRequestDto.nome())).thenReturn(Optional.empty());
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        ResponseEntity<Produto> response = produtoService.createProduto(produtoRequestDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getNome()).isEqualTo(produtoRequestDto.nome());
        verify(produtoRepository, times(1)).findByNome(produtoRequestDto.nome());
        verify(produtoRepository, times(1)).save(any(Produto.class));
    }

    @Test
    void testCreateProdutoAlreadyExists() {

        ProdutoRequestDto produtoRequestDto = new ProdutoRequestDto("Produto Existente", "Descrição Teste", BigDecimal.ONE, 1, LocalDate.now(), TipoAlimento.CEREAIS);
        Produto produto = new Produto();
        when(produtoRepository.findByNome(produtoRequestDto.nome())).thenReturn(Optional.of(produto));

        ProdutoAlreadyExistsException thrown = assertThrows(ProdutoAlreadyExistsException.class, () -> {
            produtoService.createProduto(produtoRequestDto);
        });

        assertThat(thrown.getMessage()).isEqualTo("Produto já existe com o nome: Produto Existente");
        verify(produtoRepository, times(1)).findByNome(produtoRequestDto.nome());
        verify(produtoRepository, times(0)).save(any(Produto.class));
    }

    @Test
    void testDeleteProdutoSuccess() {
        Long produtoId = 1L;
        Produto produto = new Produto();
        produto.setId(produtoId);
        produto.setNome("Produto Teste");

        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));

        ResponseEntity<Object> response = produtoService.DeleteProduto(produtoId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(produtoRepository, times(1)).findById(produtoId);
        verify(produtoRepository, times(1)).deleteById(produtoId);
    }

    @Test
    void testDeleteProdutoNotFound() {
        Long produtoId = 1L;

        when(produtoRepository.findById(produtoId)).thenReturn(Optional.empty());

        ProdutoNotFoundException thrown = assertThrows(ProdutoNotFoundException.class, () -> {
            produtoService.DeleteProduto(produtoId);
        });

        assertThat(thrown.getMessage()).isEqualTo("Produto não encontrado com id: " + produtoId);
        verify(produtoRepository, times(1)).findById(produtoId);
        verify(produtoRepository, times(0)).deleteById(produtoId);
    }

      @Test
    void testGetAllProdutoSuccess() {
        Produto produto1 = new Produto();
        produto1.setNome("Produto Teste 1");
        produto1.setDescricao("Descrição Teste 1");
        produto1.setPreco(BigDecimal.ONE);
        produto1.setDataDeValidade(LocalDate.now());
        produto1.setQuantidadeEmEstoque(10);
        produto1.setTipoAlimento(TipoAlimento.CEREAIS);

        Produto produto2 = new Produto();
        produto2.setNome("Produto Teste 2");
        produto2.setDescricao("Descrição Teste 2");
        produto2.setPreco(BigDecimal.ONE);
        produto2.setDataDeValidade(LocalDate.now());
        produto2.setQuantidadeEmEstoque(10);
        produto2.setTipoAlimento(TipoAlimento.CEREAIS);

        

        when(produtoRepository.findAll()).thenReturn(Arrays.asList(produto1, produto2));

        ResponseEntity<List<Produto>> response = produtoService.getAllProduto();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody()).contains(produto1, produto2);
        verify(produtoRepository, times(1)).findAll();
    }

    @Test
    void testGetAllProdutoNotFound() {
        when(produtoRepository.findAll()).thenReturn(Arrays.asList());

        ProdutoNotFoundException thrown = assertThrows(ProdutoNotFoundException.class, () -> {
            produtoService.getAllProduto();
        });

        assertThat(thrown.getMessage()).isEqualTo("Nenhum produto encontrado.");
        verify(produtoRepository, times(1)).findAll();
    }

    @Test
    void testUpdateProdutoSuccess() {
        Long produtoId = 1L;
        ProdutoRequestDto produtoRequestDto = new ProdutoRequestDto("Produto Teste Atualizado", "Descrição Teste", BigDecimal.ONE, 1, LocalDate.now(), TipoAlimento.CEREAIS);
        Produto produto = new Produto();
        produto.setNome("Produto Teste");
        produto.setDescricao("Descrição Teste Antigo");
        produto.setPreco(BigDecimal.ONE);
        produto.setDataDeValidade(LocalDate.now());
        produto.setQuantidadeEmEstoque(10);
        produto.setTipoAlimento(TipoAlimento.CEREAIS);

        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        ResponseEntity<Produto> response = produtoService.updateProduto(produtoId, produtoRequestDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getNome()).isEqualTo(produtoRequestDto.nome());
        verify(produtoRepository, times(1)).findById(produtoId);
        verify(produtoRepository, times(1)).save(any(Produto.class));
    }

    @Test
    void testUpdateProdutoNotFound() {
        Long produtoId = 1L;
        ProdutoRequestDto produtoRequestDto = new ProdutoRequestDto("Produto Teste Atualizado", "Descrição Teste", BigDecimal.ONE, 1, LocalDate.now(), TipoAlimento.CEREAIS);

        when(produtoRepository.findById(produtoId)).thenReturn(Optional.empty());

        ProdutoNotFoundException thrown = assertThrows(ProdutoNotFoundException.class, () -> {
            produtoService.updateProduto(produtoId, produtoRequestDto);
        });

        assertThat(thrown.getMessage()).isEqualTo("Produto não encontrado com id: " + produtoId);
        verify(produtoRepository, times(1)).findById(produtoId);
        verify(produtoRepository, times(0)).save(any(Produto.class));
    }
}
