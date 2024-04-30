package models;

import java.time.LocalDate;

public class Annonce {
    private String id_annonce;
    private String titre_a;
    private String description_a;
    private String ville_a;
    private LocalDate date_debut;
    private LocalDate date_fin;
    private String nb_jour;

    public Annonce(String id_annonce, String titre_a, String description_a, String ville_a, LocalDate date_debut, String nb_jour, LocalDate date_fin) {
        this.id_annonce = id_annonce;
        this.titre_a = titre_a;
        this.description_a = description_a;
        this.ville_a = ville_a;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.nb_jour = nb_jour;
    }

    public Annonce(String titre_a, String description_a, String ville_a, LocalDate date_debut, String nb_jour, String date_fin) {
        this.titre_a = titre_a;
        this.description_a = description_a;
        this.ville_a = ville_a;
        this.date_debut = date_debut;
        this.date_fin = LocalDate.parse(date_fin);
        this.nb_jour = nb_jour;
    }

    public Annonce() {

    }

    public String getIdAnnonce() {
        return id_annonce;
    }

    public void setIdAnnonce(String id_annonce) {
        this.id_annonce = id_annonce;
    }

    public String getTitreA() {
        return titre_a;
    }

    public void setTitreA(String titre_a) {
        this.titre_a = titre_a;
    }

    public String getDescriptionA() {
        return description_a;
    }

    public void setDescriptionA(String description_a) {
        this.description_a = description_a;
    }

    public String getVilleA() {
        return ville_a;
    }

    public void setVilleA(String ville_a) {
        this.ville_a = ville_a;
    }

    public LocalDate getDateDebut() {
        return date_debut;
    }

    public void setDateDebut(LocalDate date_debut) {
        this.date_debut = date_debut;
    }

    public String getDateFin() {
        return String.valueOf(date_fin);
    }

    public void setDateFin(LocalDate date_fin) {
        this.date_fin = date_fin;
    }

    public String getNbJour() {
        return nb_jour;
    }

    public void setNbJour(String nb_jour) {
        this.nb_jour = nb_jour;
    }
}
