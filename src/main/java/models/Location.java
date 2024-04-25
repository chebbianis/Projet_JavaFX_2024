package models;

import java.time.LocalDate;

public class Location {

    private int id_location = 0;
    private LocalDate date_debut;
    private LocalDate date_fin;
    private int user_id;
    private int voiture_id;
    private String typePaiement;
    private String options_supp;

    public Location(String options_supp, String typePaiement, int voiture_id, int user_id, LocalDate date_fin, LocalDate date_debut, int id_location) {
        this.options_supp = options_supp;
        this.typePaiement = typePaiement;
        this.voiture_id = voiture_id;
        this.user_id = user_id;
        this.date_fin = date_fin;
        this.date_debut = date_debut;
        this.id_location = id_location;
    }

    public Location(LocalDate date_debut, LocalDate date_fin, int user_id, int voiture_id, String typePaiement, String options_supp) {
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.user_id = user_id;
        this.voiture_id = voiture_id;
        this.typePaiement = typePaiement;
        this.options_supp = options_supp;
    }
    public Location() {

    }

    public int getId_location() {
        return id_location;
    }

    public LocalDate getDate_debut() {
        return date_debut;
    }

    public LocalDate getDate_fin() {
        return date_fin;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getVoiture_id() {
        return voiture_id;
    }

    public String getTypePaiement() {
        return typePaiement;
    }

    public String getOptions_supp() {
        return options_supp;
    }

    public void setId_location(int id_location) {
        this.id_location = id_location;
    }

    public void setDate_debut(LocalDate date_debut) {
        this.date_debut = date_debut;
    }

    public void setDate_fin(LocalDate date_fin) {
        this.date_fin = date_fin;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setVoiture_id(int voiture_id) {
        this.voiture_id = voiture_id;
    }

    public void setTypePaiement(String typePaiement) {
        this.typePaiement = typePaiement;
    }

    public void setOptions_supp(String options_supp) {
        this.options_supp = options_supp;
    }
}
