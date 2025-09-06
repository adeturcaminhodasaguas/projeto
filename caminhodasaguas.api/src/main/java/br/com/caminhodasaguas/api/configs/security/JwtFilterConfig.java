package br.com.caminhodasaguas.api.configs.security;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.caminhodasaguas.api.DTO.ResponseDTO;
import br.com.caminhodasaguas.api.DTO.UserDTO;
import br.com.caminhodasaguas.api.configs.exceptions.TokenInvalidException;
import br.com.caminhodasaguas.api.services.JwtService;
import br.com.caminhodasaguas.api.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilterConfig extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try{Cookie[] cookies = request.getCookies();
        String token = null;

        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("token")){
                    token = cookie.getValue();
                }
            }
        }
        if(token != null){
            String payload = jwtService.validateToken(token);
            if (payload != null) {
                ResponseDTO<UserDTO> user = userService.findById(UUID.fromString(payload));

                List<SimpleGrantedAuthority> role = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.data().role()));

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user.data(), null, role);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
              throw new TokenInvalidException("Token is invalid.");
            }
        }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally{
            filterChain.doFilter(request, response);
        }
    }
}
