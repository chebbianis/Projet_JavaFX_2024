package entities;

//import java.sql.Date;
import java.time.LocalDate;
public class Visit {
    private int id;
    private int numero;
    private String nom;
    private String email;
    private int refB;

    private LocalDate dateVisit;



    public int getId() {
        return id;
    }

    public int getNumero() {
        return numero;
    }

    public String getNom() {
        return nom;
    }

    public String getEmail() {
        return email;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public int getRefB() {
        return refB;
    }
    public LocalDate getDateVisit() {
        return dateVisit;
    }

    public void setRefB(int refB) {
        this.refB = refB;
    }


    public void setDateVisit(LocalDate dateVisit) {
        this.dateVisit = dateVisit;
    }

    public Visit() {

    }
    public Visit(int numero, String nom, String email, int refB, LocalDate dateVisit) {
        this.numero = numero;
        this.nom = nom;
        this.email = email;
        this.refB = refB;
        this.dateVisit = dateVisit;
    }
    public Visit(int id, int numero, String nom, String email, int refB, LocalDate dateVisit) {
        this.id = id;
        this.numero = numero;
        this.nom = nom;
        this.email = email;
        this.refB = refB;
        this.dateVisit = dateVisit;
    }

    @Override
    public String toString() {
        return "Visit{" +
                "id=" + id +
                ", numero=" + numero +
                ", nom='" + nom + '\'' +
                ", email='" + email + '\'' +
                ", refB=" + refB +
                ", dateVisit=" + dateVisit +
                '}';
    }
}
