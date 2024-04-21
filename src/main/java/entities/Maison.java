package entities;

public class Maison {
   private int refB ;
    private  String nom;
    private  String adresse;
    private  int nombreChambre;
    private  int prix ;
    private   String type;
    private   String image;

    public void setRefB(int refB) {
        this.refB = refB;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setNombreChambre(int nombreChambre) {
        this.nombreChambre = nombreChambre;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public int getRefB() {
        return refB;
    }

    public String getNom() {
        return nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public int getNombreChambre() {
        return nombreChambre;
    }

    public int getPrix() {
        return prix;
    }

    public String getType() {
        return type;
    }

    public String getImage() {
        return image;
    }



    public Maison(int refB, String nom, String adresse, int nombreChambre, int prix, String type, String image) {
        this.refB = refB;
        this.nom = nom;
        this.adresse = adresse;
        this.nombreChambre = nombreChambre;
        this.prix = prix;
        this.type = type;
        this.image = image;
    }

    public Maison() {
    }

    public Maison(String nom, String adresse, int nombreChambre, int prix, String type, String image) {
        this.nom = nom;
        this.adresse = adresse;
        this.nombreChambre = nombreChambre;
        this.prix = prix;
        this.type = type;
        this.image = image;
    }

    @Override
    public String toString() {
        return "Maison{" +
                "refB=" + refB +
                ", nom='" + nom + '\'' +
                ", adresse='" + adresse + '\'' +
                ", nombreChambre=" + nombreChambre +
                ", prix=" + prix +
                ", type='" + type + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
