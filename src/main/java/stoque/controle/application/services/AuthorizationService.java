package stoque.controle.application.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import stoque.controle.domain.entitys.userEntity.User;
import stoque.controle.domain.exceptions.DatabaseException;
import stoque.controle.domain.exceptions.UsuarioNotFoundException;
import stoque.controle.domain.repositories.UserRepository;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Optional<User> existUser = repository.findByEmail(username);
            return existUser.orElseThrow(() -> new UsuarioNotFoundException("Usuário inexistente ou senha inválida "));
        } catch (UsuarioNotFoundException e) {
            throw e;
        }catch (RuntimeException e) {
            throw new DatabaseException("Erro ao Buscar o Usuario.", e);
        }
       
    }

}
