package entities;
/*import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.DecimalMin;*/

public class Hotel {
    private int idH;
  //@NotBlank(message = "Veuillez saisir un nom.")
    private String nom_hotel;
   // @NotNull(message = "Veuillez sélectionner un nombre d'étoiles.")
    private int nbre_etoile;
  //  @NotBlank(message = "Veuillez sélectionner une adresse.")
    private String adresse_hotel;
  //  @NotNull(message = "Veuillez spécifier un prix par nuit.")
   // @DecimalMin(value = "0.0", inclusive = false, message = "Le prix par nuit doit être supérieur à zéro.")
    private float prix_nuit;
    private String image;

    public Hotel(int idH, String nom_hotel, int nbre_etoile, String adresse_hotel, float prix_nuit, String image) {
        this.idH = idH;
        this.nom_hotel = nom_hotel;
        this.nbre_etoile = nbre_etoile;
        this.adresse_hotel = adresse_hotel;
        this.prix_nuit = prix_nuit;
        this.image = image;
    }

    public Hotel(String nom_hotel, int nbre_etoile, String adresse_hotel, float prix_nuit, String image) {
        this.nom_hotel = nom_hotel;
        this.nbre_etoile = nbre_etoile;
        this.adresse_hotel = adresse_hotel;
        this.prix_nuit = prix_nuit;
        this.image = image;
    }

    public Hotel() {
    }

    public int getIdH() {
        return idH;
    }

    public void setIdH(int idH) {
        this.idH = idH;
    }

    public  String getNom_hotel() {
        return nom_hotel;
    }

    public void setNom_hotel(String nom_hotel) {
        this.nom_hotel = nom_hotel;
    }

    public int getNbre_etoile() {
        return nbre_etoile;
    }

    public void setNbre_etoile(int nbre_etoile) {
        this.nbre_etoile = nbre_etoile;
    }

    public String getAdresse_hotel() {
        return adresse_hotel;
    }

    public void setAdresse_hotel(String adresse_hotel) {
        this.adresse_hotel = adresse_hotel;
    }

    public float getPrix_nuit() {
        return prix_nuit;
    }

    public void setPrix_nuit(float prix_nuit) {
        this.prix_nuit = prix_nuit;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "idH=" + idH +
                ", nom_hotel='" + nom_hotel + '\'' +
                ", nbre_etoile=" + nbre_etoile +
                ", adresse_hotel='" + adresse_hotel + '\'' +
                ", prix_nuit=" + prix_nuit +
                ", image='" + image + '\'' +
                '}';
    }
}
