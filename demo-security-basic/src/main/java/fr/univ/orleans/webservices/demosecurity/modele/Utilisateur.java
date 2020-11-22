package fr.univ.orleans.webservices.demosecurity.modele;

public class Utilisateur {
    private final String login;
    private final String mdp;
    private final boolean isAdmin;

    public String getLogin() {
        return login;
    }

    public String getMdp() {
        return mdp;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public Utilisateur(String login, String mdp, boolean isAdmin) {
        this.login = login;
        this.mdp = mdp;
        this.isAdmin = isAdmin;
    }
}
