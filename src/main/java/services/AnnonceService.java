package services;

import models.Annonce;
import utils.MyDatabase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AnnonceService implements IService<Annonce> {

    @Override
    public void addAnnonce(Annonce annonce){
        String requete="INSERT INTO annonce (titre_a, description_a, ville_a, date_debut, nb_jour, date_fin) VALUES (?,?,?,?,?,?)";
        try {
            PreparedStatement pst = MyDatabase.getInstance().getConnection().prepareStatement(requete); // Assuming you have a method to get the ID from the 'annonce' object
            pst.setString(1, annonce.getTitre_a());
            pst.setString(2, annonce.getDescription_a());
            pst.setString(3, annonce.getVille_a());
            pst.setString(4, annonce.getDate_debut());
            pst.setString(5, annonce.getNb_jour());
            pst.setString(6, annonce.getDate_fin());
            pst.executeUpdate();
            System.out.println("Annonce ajoutée");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void removeAnnonce(Annonce annonce) {
        String requete = "DELETE FROM annonce WHERE id_annonce = ?";
        try {
            PreparedStatement pst = MyDatabase.getInstance().getConnection().prepareStatement(requete);
            pst.setInt(1, annonce.getId_annonce());
            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Annonce supprimée");
            } else {
                System.out.println("Aucune annonce trouvée avec cet ID");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateAnnonce(Annonce annonce) {
        String requete = "UPDATE annonce SET titre_a = ?, description_a = ?, ville_a = ?, date_debut = ?, nb_jour = ?, date_fin = ? WHERE id_annonce = ?";
        try {
            PreparedStatement pst = MyDatabase.getInstance().getConnection().prepareStatement(requete);
            pst.setString(1, annonce.getTitre_a());
            pst.setString(2, annonce.getDescription_a());
            pst.setString(3, annonce.getVille_a());
            pst.setString(4, annonce.getDate_debut());
            pst.setString(5, annonce.getNb_jour());
            pst.setString(6, annonce.getDate_fin());
            pst.setInt(7, annonce.getId_annonce());
            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Annonce mise à jour");
            } else {
                System.out.println("Aucune annonce trouvée avec cet ID");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Annonce> allData() {
        List<Annonce> data = new ArrayList<>();
        String requete = "SELECT * FROM annonce";
        try {
            Statement st = MyDatabase.getInstance().getConnection().createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()){
                Annonce a = new Annonce();
                a.setId_annonce(rs.getInt("id_annonce"));
                a.setTitre_a(rs.getString("titre_a"));
                a.setDescription_a(rs.getString("description_a"));
                a.setVille_a(rs.getString("ville_a"));
                a.setDate_debut(rs.getString("date_debut"));
                a.setNb_jour(rs.getString("nb_jour"));
                a.setDate_fin(rs.getString("date_fin"));
                data.add(a);
            }
            System.out.println("Annonces récupérées");
        } catch (SQLException e) {
            // Log the exception and handle it gracefully
            System.err.println("An error occurred while fetching data: " + e.getMessage());
            // You might want to handle this more gracefully, like returning an empty list or rethrowing the exception
            throw new RuntimeException(e);
        }
        return data;
    }

}
