package stoque.controle.infrastructure;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import jakarta.servlet.http.HttpServletResponse;
import stoque.controle.domain.entitys.userEntity.User;
import stoque.controle.domain.repositories.UserRepository;

@Service
public class TokenJWT {
    @Value("${api.security.token.secret}")
    private String secret;
    @Autowired
    UserRepository userRepository;

    private String extractedEmail;
    public String generateToken(User user, HttpServletResponse response){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getEmail())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);

                    // Cookie cookie = new Cookie("jwt", token);
                    // cookie.setHttpOnly(true); // Impede que o cookie seja acessado por JavaScript
                    // cookie.setSecure(false);
                    // cookie.setPath("/"); // Define o caminho do cookie
                    // response.addCookie(cookie);
            
                    return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while generating token", exception);
        }
    }

    public String validateToken(String token){
        try {
            extractAndStoreEmail(token);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception){
            return "";
        }
    }

    private Instant genExpirationDate(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

    private void extractAndStoreEmail(String token) {
        try {
            // Extrai o email do token
            extractedEmail  = JWT.require(Algorithm.HMAC256(secret))
                    .build()
                    .verify(token)
                    .getSubject();

            // Armazena o email onde for necessário (pode ser em uma variável de classe, no banco de dados, etc.)
            System.out.println("Email extraído do token: " + extractedEmail );
            // Você pode armazenar 'email' para uso posterior, por exemplo, em um atributo da classe ou em um serviço.
        } catch (Exception e) {
            // Lida com a exceção conforme necessário
            e.printStackTrace();
        }
    }

    public User getExtractedUser() {
        Optional<User> user = userRepository.findByEmail(extractedEmail);
        System.out.println("getExtractedUser " + extractedEmail);
        if(user.isEmpty()) return null;
        return user.get();
    }
}
