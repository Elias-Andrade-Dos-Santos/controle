package stoque.controle.application.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import stoque.controle.domain.Dtos.UserDto.AuthDTO;
import stoque.controle.domain.Dtos.UserDto.LoginResponseDTO;
import stoque.controle.domain.Dtos.UserDto.RegisteDTO;
import stoque.controle.domain.entitys.userEntity.User;
import stoque.controle.domain.repositories.UserRepository;
import stoque.controle.infrastructure.TokenJWT;

@Service
public class UserService {
    
    @Autowired
    private UserRepository repository;

    @Autowired
    private TokenJWT tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public ResponseEntity<Object> login(AuthDTO data, HttpServletResponse response){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal(), response);

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @Transactional
    public ResponseEntity<Object> register( RegisteDTO data){
        if(this.repository.findByEmail(data.email()).isEmpty()) {

            String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
            User newUser = new User(data.email(), encryptedPassword, data.role());

            this.repository.save(newUser);

            return ResponseEntity.ok().build();
        }
        else{
            return ResponseEntity.badRequest().body("perfil já cadastrado");
        }
    }


    public ResponseEntity<List<User>> getAllUser (){
        return ResponseEntity.ok().body(repository.findAll());
    }

    @Transactional
    public ResponseEntity<Object> deleteUser(UUID id){

        Optional<User> user = repository.findById(id);
        if(user.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
