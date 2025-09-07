package br.com.caminhodasaguas.api.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.caminhodasaguas.api.DTO.ResponseDTO;
import br.com.caminhodasaguas.api.DTO.UserDTO;
import br.com.caminhodasaguas.api.configs.exceptions.*;
import br.com.caminhodasaguas.api.domains.UserDomain;
import br.com.caminhodasaguas.api.domains.enums.UserEnum;
import br.com.caminhodasaguas.api.mappers.UserMapper;
import br.com.caminhodasaguas.api.repositories.UserRepository;
import br.com.caminhodasaguas.api.utils.OnlyDigitsUtils;
import br.com.caminhodasaguas.api.utils.ValidationValueUtils;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;


    public ResponseDTO<UserDTO> save(UserDTO user, UserEnum role){
        validation(user);
        existValueRegistered(user);

        UserDomain domain = UserMapper.toDomain(user);

        domain.setRole(role);

        UserDomain save = userRepository.save(domain);
        logger.info("User successfully created with name: {}", save.getName());
        return UserMapper.toDTO(save);
    }

    public ResponseDTO<List<UserDTO>> findAll(){
        List<UserDomain> users = userRepository.findAll();
        logger.info("Listing all users with size of: {}", users.size());

        return UserMapper.toListDTO(users);
    }

    public ResponseDTO<UserDTO> findById(UUID id){
        logger.info("Find by id exist user with id: {}", id);
        UserDomain user = existUser(id);
        return UserMapper.toDTO(user);
    }

    public void delete(UUID id){
        logger.info("Delete user by with id: {}", id);
        UserDomain user = existUser(id);

        userRepository.delete(user);
    }

    public ResponseDTO<UserDTO> update(UserDTO user, UUID id){
        validation(user);
        verify(user, id);

        UserDomain userExisting = existUser(id);

        UserDomain update = UserMapper.updateDomain(userExisting, user);
        UserDomain userUpdate = userRepository.save(update);

        return UserMapper.toDTO(userUpdate);
    }

    public UserDomain findByEmail(String email){
        return userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> {
                    logger.info("Find by email with value: {}", email);
                    throw new UserNotFoundException("Email não encontrado.");
                });
    }

    private void existValueRegistered(UserDTO userDTO){
        logger.info("Find if exist values is registered.");
        userRepository.findByEmailAndDeletedAtIsNull(userDTO.email())
                .ifPresent(userDomain -> {
                    logger.info("Email found with value: {}", userDomain.getEmail());
                    throw new EmailAlreadyRegisteredException("Email já cadastrado.");
                });

        userRepository.findByDocumentAndDeletedAtIsNull(OnlyDigitsUtils.normalize(userDTO.document()))
                .ifPresent(userDomain -> {
                    logger.info("Document found with value: {}", userDomain.getDocument());
                    throw new DocumentAlreadyRegisteredException("Documento já cadastrado.");
                });

        userRepository.findByPhoneAndDeletedAtIsNull(OnlyDigitsUtils.normalize(userDTO.phone()))
                .ifPresent(userDomain -> {
                    logger.info("Phone found with value: {}", userDomain.getPhone());
                    throw new PhoneAlreadyRegisteredException("Telefone já cadastrado.");
                });
    }

    private UserDomain existUser(UUID id){
        logger.info("Find if exist user with id: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.info("User not found with id: {}", id);
                    return new UserNotFoundException("Usuário não encontrado.");
                });
    }

    private void verify(UserDTO userDTO, UUID id){
        logger.info("Check if this values is from the user with id: {}", id);
        userRepository.findByEmailAndDeletedAtIsNull(userDTO.email())
                .ifPresent(userDomain -> {
                    if(!userDomain.getId().equals(id)){
                        logger.info("Email found with value: {}", userDomain.getEmail());
                        throw new EmailAlreadyRegisteredException("Email já utilizado.");
                    }
                });

        userRepository.findByDocumentAndDeletedAtIsNull(userDTO.document())
                .ifPresent(userDomain -> {
                    if(!userDomain.getId().equals(id)){
                        logger.info("Document found with value: {}", userDomain.getDocument());
                        throw new DocumentAlreadyRegisteredException("Documento já utilizado.");
                    }
                });

        userRepository.findByPhoneAndDeletedAtIsNull(userDTO.phone())
                .ifPresent(userDomain -> {
                    if(!userDomain.getId().equals(id)){
                        logger.info("Phone found with value: {}", userDomain.getDocument());
                        throw new PhoneAlreadyRegisteredException("Telefone já utilizado.");
                    }
                });
    }

    private void validation(UserDTO userDTO){
        if(!ValidationValueUtils.isValidEmail(userDTO.email())){
            logger.info("Email is invalid with value: {}", userDTO.email());
            throw new EmailInvalidException("Email inválido.");
        }

        if(!ValidationValueUtils.isValidDocument(userDTO.document())){
            logger.info("Document is invalid with value: {}", userDTO.document());
            throw new DocumentInvalidException("Documento inválido.");
        }

        if(!ValidationValueUtils.isValidAnyPhone(userDTO.phone())){
            logger.info("Phone is invalid with value: {}", userDTO.document());
            throw new PhoneInvalidException("Telefone inválido.");
        }
    }

}
