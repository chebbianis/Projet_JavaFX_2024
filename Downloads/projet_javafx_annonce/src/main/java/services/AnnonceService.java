package services;

import models.Annonce;
import utils.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnnonceService implements IService<Annonce> {

    private Connection connection;

    public AnnonceService() {
        connection = MyConnection.getInstance().getCnx();
    }

    // Méthode pour ajouter une annonce
    public void ajouter(Annonce annonce) throws SQLException {
        String sql = "INSERT INTO annonce (titre_a, description_a, ville_a, date_debut, nb_jour, date_fin) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, annonce.getTitreA());
        preparedStatement.setString(2, annonce.getDescriptionA());
        preparedStatement.setString(3, annonce.getVilleA());
        preparedStatement.setDate(4, java.sql.Date.valueOf(annonce.getDateDebut()));
        preparedStatement.setString(5, annonce.getNbJour());
        preparedStatement.setDate(6, java.sql.Date.valueOf(annonce.getDateFin()));
        preparedStatement.executeUpdate();
    }

    // Méthode pour modifier une annonce
    public void modifier(Annonce annonce) throws SQLException {
        String sql = "UPDATE annonce SET titre_a = ?, description_a = ?, ville_a = ?, date_debut = ?, nb_jour = ?, date_fin = ? WHERE id_annonce = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, annonce.getTitreA());
        preparedStatement.setString(2, annonce.getDescriptionA());
        preparedStatement.setString(3, annonce.getVilleA());
        preparedStatement.setDate(4, java.sql.Date.valueOf(annonce.getDateDebut()));
        preparedStatement.setString(5, annonce.getNbJour());
        preparedStatement.setDate(6, java.sql.Date.valueOf(annonce.getDateFin()));
        preparedStatement.setString(7, annonce.getIdAnnonce());
        preparedStatement.executeUpdate();
    }

    // Méthode pour supprimer une annonce
    public void supprimer(Annonce annonce) throws SQLException {
        String sql = "DELETE FROM annonce WHERE id_annonce = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, annonce.getIdAnnonce());
        preparedStatement.executeUpdate();
    }

    // Méthode pour afficher toutes les annonces
    public List<Annonce> afficher() throws SQLException {
        String sql = "SELECT * FROM annonce";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<Annonce> annonces = new ArrayList<>();

        while (rs.next()) {
            Annonce annonce = new Annonce();
            annonce.setIdAnnonce(rs.getString("id_annonce"));
            annonce.setTitreA(rs.getString("titre_a"));
            annonce.setDescriptionA(rs.getString("description_a"));
            annonce.setVilleA(rs.getString("ville_a"));
            annonce.setDateDebut(rs.getDate("date_debut").toLocalDate());
            annonce.setNbJour(rs.getString("nb_jour"));
            annonce.setDateFin(rs.getDate("date_fin").toLocalDate());
            annonces.add(annonce);
        }

        return annonces;
    }
}
