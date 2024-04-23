package entities;
import java.time.LocalDate;
import java.util.Objects;

public class Reservation {
    private int idR;
    private int idH;
    private int id_client;
    private LocalDate date_arrivee;
    private LocalDate date_depart;
    private String type_r;

    public int getIdH() {
        return idH;
    }

    public void setIdH(int idH) {
        this.idH = idH;
    }

    private int prix_total;
    private int nbre_chambre;
    private String type_chambre;
    private int nbre_adulte;
    private int nbre_enfant;

    public Reservation(int idR, int id_client, LocalDate date_arrivee, LocalDate date_depart, String type_r, int prix_total,int idH, int nbre_chambre, String type_chambre, int nbre_adulte, int nbre_enfant) {
        this.idR = idR;
        this.id_client = id_client;
        this.date_arrivee = date_arrivee;
        this.date_depart = date_depart;
        this.type_r = type_r;
        this.prix_total = prix_total;
        this.idH = idH;
        this.nbre_chambre = nbre_chambre;
        this.type_chambre = type_chambre;
        this.nbre_adulte = nbre_adulte;
        this.nbre_enfant = nbre_enfant;
    }

    public Reservation(int id_client, LocalDate date_arrivee, LocalDate date_depart, String type_r, int prix_total,int idH, int nbre_chambre, String type_chambre, int nbre_adulte, int nbre_enfant) {
        this.id_client = id_client;
        this.date_arrivee = date_arrivee;
        this.date_depart = date_depart;
        this.type_r = type_r;
        this.prix_total = prix_total;
        this.idH = idH;

        this.nbre_chambre = nbre_chambre;
        this.type_chambre = type_chambre;
        this.nbre_adulte = nbre_adulte;
        this.nbre_enfant = nbre_enfant;
    }

    public Reservation() {
    }

    public int getIdR() {
        return idR;
    }

    public void setIdR(int idR) {
        this.idR = idR;
    }

    public int getId_client() {
        return id_client;
    }

    public void setId_client(int id_client) {
        this.id_client = id_client;
    }

    public LocalDate getDate_arrivee() {
        return date_arrivee;
    }

    public void setDate_arrivee(LocalDate date_arrivee) {
        this.date_arrivee = date_arrivee;
    }

    public LocalDate getDate_depart() {
        return date_depart;
    }

    public void setDate_depart(LocalDate date_depart) {
        this.date_depart = date_depart;
    }

    public String getType_r() {
        return type_r;
    }

    public void setType_r(String type_r) {
        this.type_r = type_r;
    }

    public int getPrix_total() {
        return prix_total;
    }

    public void setPrix_total(int prix_total) {
        this.prix_total = prix_total;
    }

    public int getNbre_chambre() {
        return nbre_chambre;
    }

    public void setNbre_chambre(int nbre_chambre) {
        this.nbre_chambre = nbre_chambre;
    }

    public String getType_chambre() {
        return type_chambre;
    }

    public void setType_chambre(String type_chambre) {
        this.type_chambre = type_chambre;
    }

    public int getNbre_adulte() {
        return nbre_adulte;
    }

    public void setNbre_adulte(int nbre_adulte) {
        this.nbre_adulte = nbre_adulte;
    }

    public int getNbre_enfant() {
        return nbre_enfant;
    }

    public void setNbre_enfant(int nbre_enfant) {
        this.nbre_enfant = nbre_enfant;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "idR=" + idR +
                ", idH=" + idH +
                ", id_client=" + id_client +
                ", date_arrivee=" + date_arrivee +
                ", date_depart=" + date_depart +
                ", type_r='" + type_r + '\'' +
                ", prix_total=" + prix_total +
                ", nbre_chambre=" + nbre_chambre +
                ", type_chambre='" + type_chambre + '\'' +
                ", nbre_adulte=" + nbre_adulte +
                ", nbre_enfant=" + nbre_enfant +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation reservation = (Reservation) o;
        return idR == reservation.idR && idH == reservation.idH && id_client == reservation.id_client && prix_total == reservation.prix_total && nbre_chambre == reservation.nbre_chambre && nbre_adulte == reservation.nbre_adulte && nbre_enfant == reservation.nbre_enfant && Objects.equals(date_arrivee, reservation.date_arrivee) && Objects.equals(date_depart, reservation.date_depart) && Objects.equals(type_r, reservation.type_r) && Objects.equals(type_chambre, reservation.type_chambre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idR, idH, id_client, date_arrivee, date_depart, type_r, prix_total, nbre_chambre, type_chambre, nbre_adulte, nbre_enfant);
    }
}
