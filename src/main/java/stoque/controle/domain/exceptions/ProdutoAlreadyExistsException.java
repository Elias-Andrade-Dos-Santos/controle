package stoque.controle.domain.exceptions;

public class ProdutoAlreadyExistsException extends RuntimeException {
    public ProdutoAlreadyExistsException (String message){
        super(message);
    }

    public ProdutoAlreadyExistsException (String message, Throwable cause){
        super(message, cause);
    }
}
