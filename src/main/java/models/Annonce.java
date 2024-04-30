package models;

public class Annonce {
    private int id_annonce;
    private String titre_a;
    private String description_a;
    private String ville_a;
    private String date_debut;
    private String nb_jour;
    private String date_fin;

    public Annonce(int id_annonce, String titre_a, String description_a, String ville_a, String date_debut, String nb_jour, String date_fin){
        this.id_annonce = id_annonce;
        this.titre_a = titre_a;
        this.description_a = description_a;
        this.ville_a = ville_a;
        this.date_debut = date_debut;
        this.nb_jour = nb_jour;
        this.date_fin = date_fin;
    }

    public Annonce(String titre_a, String description_a, String ville_a, String date_debut, String nb_jour, String date_fin){
        this.titre_a = titre_a;
        this.description_a = description_a;
        this.ville_a = ville_a;
        this.date_debut = date_debut;
        this.nb_jour = nb_jour;
        this.date_fin = date_fin;
    }

    public Annonce() {

    }

    public int getId_annonce() {
        return id_annonce;
    }

    public void setId_annonce(int id_annonce) {
        this.id_annonce = id_annonce;
    }

    public String getTitre_a() {
        return titre_a;
    }

    public void setTitre_a(String titre_a) {
        this.titre_a = titre_a;
    }

    public String getDescription_a() {
        return description_a;
    }

    public void setDescription_a(String description_a) {
        this.description_a = description_a;
    }

    public String getVille_a() {
        return ville_a;
    }

    public void setVille_a(String ville_a) {
        this.ville_a = ville_a;
    }

    public String getDate_debut() {
        return date_debut;
    }

    public void setDate_debut(String date_debut) {
        this.date_debut = date_debut;
    }

    public String getNb_jour() {
        return nb_jour;
    }

    public void setNb_jour(String nb_jour) {
        this.nb_jour = nb_jour;
    }

    public String getDate_fin() {
        return date_fin;
    }

    public void setDate_fin(String date_fin) {
        this.date_fin = date_fin;
    }

    @Override
    public String toString() {
        return "Annonce{" +
                "id_annonce=" + id_annonce +
                ", titre_a='" + titre_a + '\'' +
                ", description_a='" + description_a + '\'' +
                ", ville_a='" + ville_a + '\'' +
                ", date_debut=" + date_debut +
                ", nb_jour=" + nb_jour +
                ", date_fin=" + date_fin +
                '}';
    }
}
