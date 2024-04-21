package com.example.pidevjava.Entities;

import java.time.LocalDate;

public class Voyage {
    private int id;
    private String programme;
    private LocalDate date_depart;
    private LocalDate date_arrive;
    private String prix;
    private String destination;
    private String image;

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


    @Override
    public String toString() {
        return "voyage{" +
                "id=" + id +
                ", programme='" + programme + '\'' +
                ", date_depart=" + date_depart +
                ", date_arrive=" + date_arrive +
                ", prix='" + prix + '\'' +
                ", destination='" + destination + '\'' +
                ", image='" + image + '\'' +
                '}';

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
