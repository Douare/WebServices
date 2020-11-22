package fr.univ.orleans.webservices.demosecurity.config;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.crypto.SecretKey;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurity extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtTokens jwtTokens;

    // Permet d'avoir accès au même encodeur partout dans le projet
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecretKey getSecretKey(){
        return Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // antMatchers permet d'effectuer du filtrage sur les urls
        // on cherche à récupérer un token d'authentification
        // sessionManagement... va permettre d'éviter de gérer les sessions avec des cookies
        http
                .csrf().disable()
                .addFilter(new JwtAuthentification(authenticationManager(), jwtTokens))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), jwtTokens))
                .authorizeRequests()
                .antMatchers(HttpMethod.GET,"/api/messages").permitAll()
                .antMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/utilisateurs").hasRole("ADMIN")
                .anyRequest().hasRole("USER")
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }
    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        // Si on ne stocke pas les utilisateurs
        /*UserDetails ju = User.builder()
                .username("ju").password("{noop}ju").roles("USER").build();
        UserDetails admin = User.builder()
                .username("admin").password("{noop}admin").roles("USER", "ADMIN").build();
        return new InMemoryUserDetailsManager(ju, admin);*/
        return new CustomUserDetailsService();
    }
}
