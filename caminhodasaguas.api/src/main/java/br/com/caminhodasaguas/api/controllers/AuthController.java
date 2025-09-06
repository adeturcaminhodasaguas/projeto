package br.com.caminhodasaguas.api.controllers;

import jakarta.servlet.http.HttpServletResponse;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import br.com.caminhodasaguas.api.DTO.CodeVerificationDTO;
import br.com.caminhodasaguas.api.DTO.EmailVerificationDTO;
import br.com.caminhodasaguas.api.DTO.ResponseDTO;
import br.com.caminhodasaguas.api.DTO.UserDTO;
import br.com.caminhodasaguas.api.domains.enums.UserEnum;
import br.com.caminhodasaguas.api.services.AuthService;
import br.com.caminhodasaguas.api.services.EmailVerificationService;
import br.com.caminhodasaguas.api.services.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private EmailVerificationService emailVerificationService;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO<UserDTO>> save(@Valid @RequestBody UserDTO userDTO) {
        ResponseDTO<UserDTO> user = userService.save(userDTO, UserEnum.ADMIN);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("/me")
    public ResponseEntity<ResponseDTO<UserDTO>> me(@AuthenticationPrincipal UserDTO usuario){
        ResponseDTO<UserDTO> user = userService.findById(usuario.id());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/email-verification")
    public ResponseEntity<ResponseDTO<Boolean>> emailVerification(
            @Valid @RequestBody EmailVerificationDTO emailVerificationDTO) {
        ResponseDTO<Boolean> vefication = emailVerificationService.sendEmailVerification(emailVerificationDTO);
        return ResponseEntity.ok(vefication);
    }

    @PostMapping("/code-verification")
    public ResponseEntity<ResponseDTO<String>> codeVerification(@Valid @RequestBody CodeVerificationDTO codeVerificationDTO, HttpServletResponse response){
        ResponseDTO<String> token = emailVerificationService.codeVerification(codeVerificationDTO, response);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseDTO<Boolean>> logout(HttpServletResponse response){
        ResponseDTO<Boolean> success = authService.logout(response);
        return ResponseEntity.ok(success);
    }

}
