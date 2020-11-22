package fr.univ.orleans.webservices.demosecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import javax.websocket.Session;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurity extends WebSecurityConfigurerAdapter {
    // Permet d'avoir accès au même encodeur partout dans le projet
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
/*    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // permet de définir comment les utilisateurs vont être authentifiés
        // stock en dur dans la mémoire, c'est OK pour une DEMO
        auth.inMemoryAuthentication()
                .withUser("ju").password("{noop}ju").roles("USER")
                .and()
                .withUser("admin").password("{noop}admin").roles("USER","ADMIN");
        // entre accolades, devant le mdp, on peut préciser la manière dont il est encodé
    }*/
//Incompatible avec UserDetailsService

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // antMatchers permet d'effectuer du filtrage sur les urls
        // httpBasic renvoie l'id et le mdp à chq requête
        // sessionManagement... va permettre d'éviter de gérer les sessions avec des cookies
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET,"/api/messages").permitAll()
                .antMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/utilisateurs").hasRole("ADMIN")
                .anyRequest().hasRole("USER")
                .and().httpBasic()
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
