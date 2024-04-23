package com.example.pi.Entities;

import java.sql.Date;

public class Evenement {
    private String id_evenement;
    private String titre_e;
    private String description_e;
    private String ville_e;
    private Date date_debut_e;
    private Date date_fin_e;
    private String nb_jour_e;

    public Evenement(String id_evenement, String titre_e, String description_e, String ville_e, Date date_debut_e, Date date_fin_e, String nb_jour_e) {
        this.id_evenement = id_evenement;
        this.titre_e = titre_e;
        this.description_e = description_e;
        this.ville_e = ville_e;
        this.date_debut_e = date_debut_e;
        this.date_fin_e = date_fin_e;
        this.nb_jour_e = nb_jour_e;
    }

    public String getIdEvenement() {
        return id_evenement;
    }

    public void setIdEvenement(String id_evenement) {
        this.id_evenement = id_evenement;
    }

    public String getTitreE() {
        return titre_e;
    }

    public void setTitreE(String titre_e) {
        this.titre_e = titre_e;
    }

    public String getDescriptionE() {
        return description_e;
    }

    public void setDescriptionE(String description_e) {
        this.description_e = description_e;
    }

    public String getVilleE() {
        return ville_e;
    }

    public void setVilleE(String ville_e) {
        this.ville_e = ville_e;
    }

    public Date getDateDebutE() {
        return date_debut_e;
    }

    public void setDateDebutE(Date date_debut_e) {
        this.date_debut_e = date_debut_e;
    }

    public Date getDateFinE() {
        return date_fin_e;
    }

    public void setDateFinE(Date date_fin_e) {
        this.date_fin_e = date_fin_e;
    }

    public String getNbJourE() {
        return nb_jour_e;
    }

    public void setNbJourE(String nb_jour_e) {
        this.nb_jour_e = nb_jour_e;
    }
}
