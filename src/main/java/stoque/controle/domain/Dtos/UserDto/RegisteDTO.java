package stoque.controle.domain.Dtos.UserDto;

import java.util.UUID;

import stoque.controle.domain.entitys.userEntity.UserRole;

public record RegisteDTO(UUID id,String email, String password,UserRole role) {
    
}
