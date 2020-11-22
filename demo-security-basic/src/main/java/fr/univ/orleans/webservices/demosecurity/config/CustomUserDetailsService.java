package fr.univ.orleans.webservices.demosecurity.config;

import fr.univ.orleans.webservices.demosecurity.controller.MessageController;
import fr.univ.orleans.webservices.demosecurity.facade.Facade;
import fr.univ.orleans.webservices.demosecurity.modele.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

public class CustomUserDetailsService implements UserDetailsService {
    private static final String[] ROLE_ADMIN = {"USER","ADMIN"};
    private static final String[] ROLE_USER = {"USER"};
    private static Facade facade = MessageController.getFacade();
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Utilisateur utilisateur = facade.getUtilisateur(s);
        if (utilisateur == null){
            throw new UsernameNotFoundException("L'utilisateur "+s+" est introuvable.");
        }
        String[] roles = utilisateur.getIsAdmin() ? ROLE_ADMIN : ROLE_USER;
        UserDetails userDetails = User.builder()
                .username(utilisateur.getLogin()).password(passwordEncoder.encode(utilisateur.getMdp())).roles(roles)
                .build();
        return userDetails;
    }
}
