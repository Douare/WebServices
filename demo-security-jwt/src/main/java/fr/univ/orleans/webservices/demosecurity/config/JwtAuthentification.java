package fr.univ.orleans.webservices.demosecurity.config;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthentification extends UsernamePasswordAuthenticationFilter {
    private JwtTokens jwtTokens;
    public JwtAuthentification(AuthenticationManager authenticationManager, JwtTokens jwtTokens) {
        setAuthenticationManager(authenticationManager);
        setFilterProcessesUrl("/api/login");
        this.jwtTokens = jwtTokens;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        //appelée quand l'authentification est un succès
        UserDetails user = (UserDetails)authResult.getPrincipal();
        // fabrication d'un token
        String token = jwtTokens.genereToken(user);
        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer "+token);
    }
}
