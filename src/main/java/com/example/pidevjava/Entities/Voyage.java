package com.example.pidevjava.Entities;

import java.time.LocalDate;
import java.util.List;

public class Voyage {
    private int id;
    private String programme;
    private LocalDate date_depart;
    private LocalDate date_arrive;
    private String prix;
    private String destination;
    private String image;
    private List<Voyageur> voyageurs;

    // Constructeurs, getters et setters existants...

    public List<Voyageur> getVoyageurs() {
        return voyageurs;
    }

    public void setVoyageurs(List<Voyageur> voyageurs) {
        this.voyageurs = voyageurs;
    }



    public void addVoyageur(Voyageur voyageur) {
        voyageurs.add(voyageur);
    }

    public void removeVoyageur(Voyageur voyageur) {
        voyageurs.remove(voyageur);
    }

    public Voyageur getVoyageurById(int voyageurId) {
        for (Voyageur voyageur : voyageurs) {
            if (voyageur.getId() == voyageurId) {
                return voyageur;
            }
        }
        return null;
    }

    public boolean hasVoyageur(Voyageur voyageur) {
        return voyageurs.contains(voyageur);
    }


    @Override
    public String toString() {
        return "Voyage{" +
                "id=" + id +
                ", programme='" + programme + '\'' +
                ", date_depart=" + date_depart +
                ", date_arrive=" + date_arrive +
                ", prix='" + prix + '\'' +
                ", destination='" + destination + '\'' +
                ", image='" + image + '\'' +
                ", voyageurs=" + voyageurs +
                '}';
    }

    public Voyage() {

    }

    public Voyage(String programme, LocalDate dateDepart, LocalDate dateArrive, String prix, String destination) {
        this.programme = programme;
        this.date_depart = dateDepart;
        this.date_arrive = dateArrive;
        this.prix = prix;
        this.destination = destination;

    }

    public int getId() {
        return id;
    }

    public String getProgramme() {
        return programme;
    }

    public LocalDate getDateDepart() {
        return date_depart;
    }

    public LocalDate getDateArrive() {
        return date_arrive;
    }

    public String getPrix() {
        return prix;
    }

    public String getDestination() {
        return destination;
    }

    public String getImage() {
        return image;
    }
    public Voyage(String programme, LocalDate date_depart, LocalDate date_arrive, String prix, String destination, String image)
    {
        this.programme = programme;
        this.date_depart = date_depart;
        this.date_arrive = date_arrive;
        this.prix = prix;
        this.destination = destination;
        this.image = image;
    }
    public Voyage(String programme, String prix) {
        this.programme = programme;
        this.prix = prix;
    }




    public void setId(int id) {
        this.id = id;
    }

    public void setProgramme(String programme) {
        this.programme = programme;
    }

    public void setDateDepart(LocalDate date_depart) {
        this.date_depart = date_depart;
    }

    public void setDateArrive(LocalDate date_arrive) {
        this.date_arrive = date_arrive;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setImage(String image) {
        this.image = image;
    }



}
