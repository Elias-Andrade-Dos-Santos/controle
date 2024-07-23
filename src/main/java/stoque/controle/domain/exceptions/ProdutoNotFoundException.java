package stoque.controle.domain.exceptions;

public class ProdutoNotFoundException extends RuntimeException{
    public ProdutoNotFoundException (String message){
        super(message);
    }

    public ProdutoNotFoundException (String message, Throwable cause){
        super( message,  cause);
    }
}
