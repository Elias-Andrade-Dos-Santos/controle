package stoque.controle.Presentation.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import stoque.controle.domain.exceptions.DatabaseException;
import stoque.controle.domain.exceptions.ProdutoAlreadyExistsException;
import stoque.controle.domain.exceptions.ProdutoNotFoundException;
import stoque.controle.domain.exceptions.UsuarioNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler extends  ResponseEntityExceptionHandler{
    @ExceptionHandler(ProdutoNotFoundException.class)
    public ResponseEntity<String> handleProdutoNotFoundException(ProdutoNotFoundException ex, WebRequest request) {
        String errorMessage = "Erro: " + ex.getMessage() + ". Causa: " + (ex.getCause() != null ? ex.getCause() : "Nenhuma");
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProdutoAlreadyExistsException.class)
    public ResponseEntity<String> handleProdutoAlreadyExistsException(ProdutoAlreadyExistsException ex, WebRequest request) {
        String errorMessage = "Erro: " + ex.getMessage() + ". Causa: " + (ex.getCause() != null ? ex.getCause() : "Nenhuma");
        return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<String> handleDatabaseException(DatabaseException ex, WebRequest request) {
        String errorMessage = "Erro: " + ex.getMessage() + ". Causa: " + (ex.getCause() != null ? ex.getCause() : "Nenhuma");
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(UsuarioNotFoundException.class)
    public ResponseEntity<String> handleUsuarioNotFoundException(UsuarioNotFoundException ex, WebRequest request) {
        String errorMessage = "Erro: " + ex.getMessage() + ". Causa: " + (ex.getCause() != null ? ex.getCause() : "Nenhuma");
        return new ResponseEntity<>(errorMessage, HttpStatus.UNAUTHORIZED);
    }

    // @ExceptionHandler(Exception.class)
    // public ResponseEntity<String> handleGlobalException(Exception ex, WebRequest request) {
    //     String errorMessage = "Ocorreu um erro inesperado: " + ex.getMessage() + ". Causa: " + (ex.getCause() != null ? ex.getCause() : "Nenhuma");
    //     return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    // }


}
