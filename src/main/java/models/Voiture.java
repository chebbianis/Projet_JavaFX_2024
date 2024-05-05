package models;

import java.time.LocalDate;
import java.util.Date;

public class Voiture {
    private int id_voiture = 0;
    private String marque;
    private LocalDate annee;
    private int prix_j;
    private int kilometrage;
    private int nbrPlaces;


    public Voiture(String marque, LocalDate annee, int prix_j, int kilometrage, int nbrPlaces) {
        this.marque = marque;
        this.annee = annee;
        this.prix_j = prix_j;
        this.kilometrage = kilometrage;
        this.nbrPlaces = nbrPlaces;
    }

    public Voiture(int id_voiture, String marque, LocalDate annee, int prix_j,  int kilometrage, int nbrPlaces) {
        this.id_voiture = id_voiture;
        this.marque = marque;
        this.annee = annee;
        this.prix_j = prix_j;
        this.kilometrage = kilometrage;
        this.nbrPlaces = nbrPlaces;

    }

    public Voiture() {
    }

    public int getId_voiture() {
        return id_voiture;
    }

    public String getMarque() {
        return marque;
    }

    public LocalDate getAnnee() {
        return annee;
    }

    public int getPrix_j() {
        return prix_j;
    }

    public void setId_voiture(int id_voiture) {
        this.id_voiture = id_voiture;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public void setAnnee(LocalDate annee) {
        this.annee = annee;
    }

    public void setPrix_j(int prix_j) {
        this.prix_j = prix_j;
    }

    public int getKilometrage() {
        return kilometrage;
    }

    public int getNbrPlaces() {
        return nbrPlaces;
    }

    public void setKilometrage(int kilometrage) {
        this.kilometrage = kilometrage;
    }

    public void setNbrPlaces(int nbrPlaces) {
        this.nbrPlaces = nbrPlaces;
    }

    @Override
    public String toString() {
        return "Voiture{" +
                "id_voiture=" + id_voiture +
                ", marque='" + marque + '\'' +
                ", annee=" + annee +
                ", prix_j=" + prix_j +
                ", kilometrage=" + kilometrage +
                ", nbrPlaces=" + nbrPlaces +
                '}';
    }
}
