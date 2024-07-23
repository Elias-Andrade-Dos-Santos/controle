package stoque.controle.application.services;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import stoque.controle.domain.Dtos.ProdutoDtos.ProdutoRequestDto;
import stoque.controle.domain.entitys.produtoEntity.Produto;
import stoque.controle.domain.exceptions.DatabaseException;
import stoque.controle.domain.exceptions.ProdutoAlreadyExistsException;
import stoque.controle.domain.exceptions.ProdutoNotFoundException;
import stoque.controle.domain.repositories.ProdutoRepository;

@Service
public class ProdutoService {
    @Autowired
    private ProdutoRepository produtoRepository;

    @Transactional
    public ResponseEntity<Produto> createProduto(ProdutoRequestDto produtoRequestDto){
        try {
            produtoRepository.findByNome(produtoRequestDto.nome())
                .ifPresent(produto -> {
                    throw new ProdutoAlreadyExistsException("Produto já existe com o nome: " + produtoRequestDto.nome());
                });
            
            Produto newProduto = new Produto();
            BeanUtils.copyProperties(produtoRequestDto, newProduto);
            produtoRepository.save(newProduto);
            return ResponseEntity.status(HttpStatus.CREATED).body(newProduto);
        } catch (ProdutoAlreadyExistsException e) {
            throw e; // Relança a exceção específica
        } catch (RuntimeException e) {
            throw new DatabaseException("Erro ao criar o produto.", e);
        }
        
    }

    public ResponseEntity<List<Produto>> getAllProduto(){
        try {
            List<Produto> produtos = produtoRepository.findAll();
            if (produtos.isEmpty()) {
                throw new ProdutoNotFoundException("Nenhum produto encontrado.");
            }
            return ResponseEntity.status(HttpStatus.OK).body(produtos);
        } catch (ProdutoNotFoundException e) {
            throw e; // Relança a exceção específica
        } catch (RuntimeException e) {
            throw new DatabaseException("Erro ao buscar produtos.", e);
        }
    }
    
    @Transactional
    public ResponseEntity<Produto> updateProduto(Long id, ProdutoRequestDto produtoRequestDto){
        try {
            Produto produtoOptional =produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNotFoundException("Produto não encontrado com id: " + id));
            
            Produto produto = produtoOptional;
            BeanUtils.copyProperties(produtoRequestDto, produto);
            Produto updatedProduto = produtoRepository.save(produto);
            return ResponseEntity.status(HttpStatus.OK).body(updatedProduto);
        } catch (ProdutoNotFoundException e) {
            throw e; // Relança a exceção específica
        } catch (RuntimeException e) {
            throw new DatabaseException("Erro ao atualizar o produto.", e);
        }
    }

    @Transactional
    public ResponseEntity<Object> DeleteProduto(Long id){
        try {
            produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNotFoundException("Produto não encontrado com id: " + id));
            
            produtoRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (ProdutoNotFoundException e) {
            throw e; // Relança a exceção específica
        } catch (RuntimeException e) {
            throw new DatabaseException("Erro ao deletar o produto.", e);
        }
    }

}
