package br.com.caminhodasaguas.api.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.caminhodasaguas.api.DTO.ResponseDTO;
import br.com.caminhodasaguas.api.DTO.UserDTO;
import br.com.caminhodasaguas.api.services.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<UserDTO>>> findAll() {
        ResponseDTO<List<UserDTO>> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<UserDTO>> findById(@PathVariable("id") UUID id) {
        ResponseDTO<UserDTO> user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<UserDTO>> update(@PathVariable("id") UUID id,@Valid @RequestBody UserDTO userDTO) {
        ResponseDTO<UserDTO> user = userService.update(userDTO, id);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<UserDTO>> delete(@PathVariable("id") UUID id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
