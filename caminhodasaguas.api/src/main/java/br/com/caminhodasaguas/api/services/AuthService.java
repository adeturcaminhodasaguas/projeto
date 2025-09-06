package br.com.caminhodasaguas.api.services;

import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.caminhodasaguas.api.DTO.ResponseDTO;

@Service
public class AuthService {

    Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private CookieService cookieService;

    public ResponseDTO<Boolean> logout(HttpServletResponse response){
        cookieService.removeCookie("token", response);

        logger.info("logout successfully.");
        return new ResponseDTO<Boolean>(true);
    }


}
