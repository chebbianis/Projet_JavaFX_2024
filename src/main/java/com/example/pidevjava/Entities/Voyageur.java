package com.example.pidevjava.Entities;


import java.time.LocalDate;

public class Voyageur {

    private int id;
    private int num_pass;
    private String nom;
    private String prenom;
    private int age;
    private String etat_civil;
    private String email;
    private LocalDate date_nais;


    private Voyage voyage;


    public Voyage getVoyage() {
        return voyage;
    }

    public void setVoyage(Voyage voyage) {
        this.voyage = voyage;
    }

    public Voyageur() {

    }

    public Voyageur (int num_pass, String nom, String prenom, int age, String etat_civil, String email) {
        this.num_pass = num_pass;
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
        this.etat_civil = etat_civil;
        this.email = email;
    }

    public Voyageur(int num_pass, String nom, String prenom, int age, String etat_civil, String email, LocalDate date_nais) {
        this.num_pass = num_pass;
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
        this.etat_civil = etat_civil;
        this.email = email;
        this.date_nais = date_nais;
    }
    public Voyageur(int num_pass, String nom, String prenom, String etat_civil, String email, LocalDate date_nais) {
        this.num_pass = num_pass;
        this.nom = nom;
        this.prenom = prenom;
        this.etat_civil = etat_civil;
        this.email = email;
        this.date_nais = date_nais;
    }

    public Voyageur(int id, int num_pass, String nom, String prenom, int age, String etat_civil, String email, LocalDate date_nais) {
        this.id = id;
        this.num_pass = num_pass;
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
        this.etat_civil = etat_civil;
        this.email = email;
        this.date_nais = date_nais;
    }

    public int getId() {
        return id;
    }
    public int getNum_pass() {
        return num_pass;
    }

    public String getNom() {return nom;}
    public String getPrenom() {return prenom;}
    public int getAge() {
        return age;
    }
    public String getEtat_civil() {return etat_civil;}
    public String getEmail() {return email;}
    public LocalDate getDate_nais() {
        return date_nais;
    }


    @Override
    public String toString() {
        return "Voyageur{" +
                "id=" + id +
                ", num_pass=" + num_pass +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", age=" + age +
                ", etat_civil='" + etat_civil + '\'' +
                ", email='" + email + '\'' +
                ", date_nais='" + date_nais + '\'' +
                ", voyage=" + voyage +
                '}';
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setNum_pass(int num_pass) {
        this.num_pass = num_pass;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    public void setPrenom(String prenom) {this.prenom = prenom;}
    public void setAge(int age) {
        this.age = age;
    }

    public void setEtat_civil(String etat_civil) {this.etat_civil = etat_civil;}
    public void setEmail(String email) {this.email = email;}
    public void setDate_nais(LocalDate date_nais) {
        this.date_nais= date_nais;
    }


}
