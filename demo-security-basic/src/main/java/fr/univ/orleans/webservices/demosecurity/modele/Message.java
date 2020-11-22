package fr.univ.orleans.webservices.demosecurity.modele;

public class Message {
    private final long id;
    private final String txt;

    public long getId() {
        return id;
    }

    public String getTxt() {
        return txt;
    }

    public Message(long id, String txt) {
        this.id = id;
        this.txt = txt;
    }
}
