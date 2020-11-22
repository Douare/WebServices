package fr.univ.orleans.webservices.demosecurity.facade;

import fr.univ.orleans.webservices.demosecurity.modele.Message;
import fr.univ.orleans.webservices.demosecurity.modele.Utilisateur;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;

public class Facade {
    private List<Message> messages = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong(1L);
    private Map<String, Utilisateur> utilisateurs = new TreeMap<>();;

    public List<Message> getMessages() {
        return messages;
    }

    public AtomicLong getCounter() {
        return counter;
    }

    public Map<String, Utilisateur> getUtilisateurs() {
        return utilisateurs;
    }

    public Utilisateur getUtilisateur(String login) {
        return utilisateurs.get(login);
    }

    public Facade() {
        utilisateurs.put("ju", new Utilisateur("ju", "ju", false));
        utilisateurs.put("admin", new Utilisateur("admin", "admin", true));
    }
}
