package br.com.caminhodasaguas.api.mappers;

import java.util.List;

import br.com.caminhodasaguas.api.DTO.ResponseDTO;
import br.com.caminhodasaguas.api.DTO.UserDTO;
import br.com.caminhodasaguas.api.domains.UserDomain;
import br.com.caminhodasaguas.api.utils.OnlyDigitsUtils;

public class UserMapper {

    public static UserDomain toDomain(UserDTO userDTO){
        UserDomain user = new UserDomain();
        user.setName(userDTO.name());
        user.setEmail(userDTO.email());
        user.setDocument(OnlyDigitsUtils.normalize(userDTO.document()));
        user.setPhone(OnlyDigitsUtils.normalize(userDTO.phone()));
        user.setRole(userDTO.role());
        return user;
    }

    public static ResponseDTO<UserDTO> toDTO(UserDomain userDomain){
        return new ResponseDTO<UserDTO>(
                new UserDTO(
                        userDomain.getId(),
                        userDomain.getName(),
                        userDomain.getEmail(),
                        userDomain.getDocument(),
                        userDomain.getPhone(),
                        userDomain.getRole(),
                        userDomain.getDeletedAt()
                )
        );
    }

    public static ResponseDTO<List<UserDTO>> toListDTO(List<UserDomain> domains){
        List<UserDTO> users = domains.stream()
                .map(u -> new UserDTO(
                        u.getId(),
                        u.getName(),
                        u.getEmail(),
                        u.getDocument(),
                        u.getPhone(),
                        u.getRole(),
                        u.getDeletedAt()
                ))
                .toList();
        return new ResponseDTO<List<UserDTO>>(users);
    }

    public static UserDomain updateDomain(UserDomain userDomain, UserDTO userDTO){
        userDomain.setName(userDTO.name());
        userDomain.setEmail(userDTO.email());
        userDomain.setPhone(userDTO.phone());
        userDomain.setDocument(userDTO.document());
        return userDomain;
    }
}
