package br.com.caminhodasaguas.api.services;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.caminhodasaguas.api.DTO.CodeVerificationDTO;
import br.com.caminhodasaguas.api.DTO.EmailVerificationDTO;
import br.com.caminhodasaguas.api.DTO.ResponseDTO;
import br.com.caminhodasaguas.api.configs.exceptions.CodeInvalidException;
import br.com.caminhodasaguas.api.configs.exceptions.EmailInvalidException;
import br.com.caminhodasaguas.api.configs.exceptions.EmailNotFoundException;
import br.com.caminhodasaguas.api.configs.exceptions.ExpiredcodeException;
import br.com.caminhodasaguas.api.domains.EmailVerificationDomain;
import br.com.caminhodasaguas.api.domains.UserDomain;
import br.com.caminhodasaguas.api.repositories.EmailVerificationRepository;
import br.com.caminhodasaguas.api.utils.CodeGeneratorUtils;
import br.com.caminhodasaguas.api.utils.ValidationValueUtils;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Optional;

@Service
public class EmailVerificationService {

    Logger logger = LoggerFactory.getLogger(EmailVerificationService.class);

    @Autowired
    private EmailVerificationRepository emailVerificationRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private CookieService cookieService;

    @Value("${spring.mail.expiration}")
    private int expiration;

    @Transactional
    public ResponseDTO<Boolean> sendEmailVerification(EmailVerificationDTO emailVerificationDTO) {
        validation(emailVerificationDTO.email());
        userService.findByEmail(emailVerificationDTO.email());
        try {
            String code = CodeGeneratorUtils.generator();
            String encoded = passwordEncoder.encode(code);

            Duration minutes = Duration.ofMinutes(expiration);

            EmailVerificationDomain domain = existEmail(emailVerificationDTO.email())
                    .orElse(new EmailVerificationDomain());

            domain.setEmail(emailVerificationDTO.email());
            domain.setCode(encoded);
            domain.setExp(minutes);
            domain.setChecked(false);
            domain.setCreatedAt(OffsetDateTime.now());

            emailVerificationRepository.save(domain);

            mailService.sendEmail(
                    emailVerificationDTO.email(),
                    "Verificação",
                    code
            );

            logger.info("Email verification generated successfully");
            return new ResponseDTO<Boolean>(true);
        } catch (Exception e) {
            logger.error("Error generating email verification", e);
            return new ResponseDTO<Boolean>(false);
        }
    }

    public ResponseDTO<String> codeVerification(CodeVerificationDTO codeVerificationDTO, HttpServletResponse response) {
        EmailVerificationDomain email = existEmail(codeVerificationDTO.email())
                .orElseThrow(() -> new EmailNotFoundException("Email não encontrado."));

        OffsetDateTime expiresAt = email.getCreatedAt().plus(email.getExp());

        if (email.isChecked()) {
            throw new ExpiredcodeException("Código já utilizado.");
        }

        if (OffsetDateTime.now().isAfter(expiresAt)) {
            throw new ExpiredcodeException("Código expirado.");
        }

        if (!passwordEncoder.matches(codeVerificationDTO.code(), email.getCode())) {
            logger.info("Invalid code for email: {}", codeVerificationDTO.email());
            throw new CodeInvalidException("Código inválido.");
        }

        email.setChecked(true);
        emailVerificationRepository.save(email);

        String token = token(codeVerificationDTO.email());

        cookieService.addCookie("token", token, response);

        return new ResponseDTO<String>(token);
    }

    private String token(String email){
        UserDomain user = userService.findByEmail(email);
        return jwtService.generateToken(user);
    }

    private Optional<EmailVerificationDomain> existEmail(String email){
        logger.info("Find the verification code by email with value: {}", email);
        return emailVerificationRepository.findByEmail(email);
    }

    private void validation(String email){
        logger.info("Verification email with value: {}", email);
        if(!ValidationValueUtils.isValidEmail(email)){
            logger.info("Email is invalid.");
            throw new EmailInvalidException("Email inválido.");
        }
    }
}
