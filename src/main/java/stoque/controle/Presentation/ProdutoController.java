package stoque.controle.Presentation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import stoque.controle.application.services.ProdutoService;
import stoque.controle.domain.Dtos.ProdutoDtos.ProdutoRequestDto;
import stoque.controle.domain.entitys.produtoEntity.Produto;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {
@Autowired
    private ProdutoService produtoService;

    @PostMapping
    public ResponseEntity<Produto> createProduto(@RequestBody ProdutoRequestDto produtoRequestDto) {
        return produtoService.createProduto(produtoRequestDto);
    }

    @GetMapping
    public ResponseEntity<List<Produto>> getAllProduto() {
        return produtoService.getAllProduto();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> updateProduto(@PathVariable Long id, @RequestBody ProdutoRequestDto produtoRequestDto) {
        return produtoService.updateProduto(id, produtoRequestDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduto(@PathVariable Long id) {
        return produtoService.DeleteProduto(id);
    }
}
