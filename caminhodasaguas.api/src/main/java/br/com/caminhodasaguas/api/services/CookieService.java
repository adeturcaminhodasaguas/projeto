package br.com.caminhodasaguas.api.services;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CookieService {

    Logger logger = LoggerFactory.getLogger(CookieService.class);

    public void addCookie(String name, String value,HttpServletResponse response){
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        logger.info("Token add successfully");
    }

    public void removeCookie(String name, HttpServletResponse response){
        Cookie cookie = new Cookie(name, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
