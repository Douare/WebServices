package fr.univ.orleans.webservices.demosecurity.controller;

import fr.univ.orleans.webservices.demosecurity.facade.Facade;
import fr.univ.orleans.webservices.demosecurity.modele.Message;
import fr.univ.orleans.webservices.demosecurity.modele.Utilisateur;
import jdk.jshell.execution.Util;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.swing.*;
import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

@RestController
@RequestMapping("/api")
public class MessageController {
    private static Facade facade = new Facade();
    private static List<Message> messages = facade.getMessages();
    private Map<String, Utilisateur> utilisateurs = facade.getUtilisateurs();
    private final AtomicLong counter = facade.getCounter();

    public static Facade getFacade() {
        return facade;
    }

    @PostMapping("/messages")
    public ResponseEntity<Message> create(Principal principal, @RequestBody Message message){
        String login = principal.getName();
        Message messageRec = new Message(counter.getAndIncrement(), login+" : "+message.getTxt());
        messages.add(messageRec);

        // Fabrication de l'url de la nouvelle ressource crée (messageRec)
        // On y associe l'id qu'on vient de créer grâce à l'autoincrémentation de counter
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(messageRec.getId())
                .toUri();

        // La réponse (201) contient location en entête et messaeRec (et son id) dans son body
        return ResponseEntity.created(location).body(messageRec);
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAll(){
        return ResponseEntity.ok().body(messages);
    }

    @GetMapping("/messages/{id}")
    public ResponseEntity<Message> findById(@PathVariable("id") Long id){
        Optional<Message> message = messages.stream().filter(m->m.getId()==id).findAny();
        if (message.isPresent()){
            return ResponseEntity.ok().body(message.get());
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/messages/{id}")
    public ResponseEntity deleteById(@PathVariable("id") Long id){
        for(int i=0; i<messages.size();i++){
            if (messages.get(i).getId()==id){
                messages.remove(i);
                return ResponseEntity.noContent().build();
            }
        }
        return ResponseEntity.notFound().build();
    }


    // UTILISATEURS ----------------------------------------------------------------------------------------------------
    @PostMapping("/utilisateurs")
    public ResponseEntity<Utilisateur> create(@RequestBody Utilisateur utilisateur){
        String login = utilisateur.getLogin();
        Predicate<String> isValid = s -> (s != null) && (s.length()>1);

        if (isValid.test(login) && isValid.test(utilisateur.getMdp())){
            if (utilisateurs.containsKey(login)){
                return ResponseEntity.badRequest().build();
            }
            utilisateurs.put(login, utilisateur);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(login)
                    .toUri();

            return ResponseEntity.created(location).body(utilisateur);
        }
        return ResponseEntity.badRequest().build();
    }

    /*@GetMapping("/utilisateurs/{id}")
    public ResponseEntity<Utilisateur> findByLogin(Principal principal, @PathVariable("id") String id){
        if (!principal.getName().equals(id)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Utilisateur utilisateur = utilisateurs.get(id);
        if (utilisateur != null){
            return ResponseEntity.ok().body(utilisateur);
        }else{
            return ResponseEntity.notFound().build();
        }
    }*/

    @GetMapping("/utilisateurs/{id}")
    @PreAuthorize("#id == authentication.principal.username")
    public ResponseEntity<Utilisateur> findByLogin(@PathVariable("id") String id){
        Utilisateur utilisateur = utilisateurs.get(id);
        if (utilisateur != null){
            return ResponseEntity.ok().body(utilisateur);
        }else{
            return ResponseEntity.notFound().build();
        }
    }
}
