package stoque.controle.Presentation;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import stoque.controle.application.services.UserService;
import stoque.controle.domain.Dtos.UserDto.AuthDTO;
import stoque.controle.domain.Dtos.UserDto.RegisteDTO;
import stoque.controle.domain.entitys.userEntity.User;



@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid AuthDTO data, HttpServletResponse response){
        return userService.login(data, response);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUser(){
        return userService.getAllUser();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable UUID id){
        return userService.deleteUser(id);
    }

  

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody @Valid RegisteDTO data){
        return userService.register(data);
    }
}
