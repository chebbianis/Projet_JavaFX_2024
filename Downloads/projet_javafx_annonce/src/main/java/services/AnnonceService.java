package services;

import models.Annonce;
import models.User;
import utils.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnnonceService implements IService<Annonce> {

    private Connection connection;

    public AnnonceService() {
        connection = MyConnection.getInstance().getCnx();
    }

    public User getUserById(String userId) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        User user = null;

        try {
            String query = "SELECT * FROM user WHERE id = ?";
            stmt = connection.prepareStatement(query);
            stmt.setString(1, userId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                // Set other user attributes as needed
            }
        } finally {
            // Close resources
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }

        return user;
    }

    // Méthode pour ajouter une annonce
    public void ajouter(Annonce annonce) throws SQLException {
        String sql = "INSERT INTO annonce (titre_a, description_a, ville_a, date_debut, nb_jour, date_fin, user_id, maps_link) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, annonce.getTitreA());
            preparedStatement.setString(2, annonce.getDescriptionA());
            preparedStatement.setString(3, annonce.getVilleA());
            preparedStatement.setDate(4, java.sql.Date.valueOf(annonce.getDateDebut()));
            preparedStatement.setString(5, annonce.getNbJour());
            preparedStatement.setDate(6, java.sql.Date.valueOf(annonce.getDateFin()));
            preparedStatement.setString(7, annonce.getUser());
            preparedStatement.setString(8, annonce.getMapsLink());
            preparedStatement.executeUpdate();
            String mapsLink = generateMapsLink(annonce.getVilleA());
            annonce.setMapsLink(mapsLink);
        }
    }

    // Méthode pour modifier une annonce
    public void modifier(Annonce annonce) throws SQLException {
        String sql = "UPDATE annonce SET titre_a = ?, description_a = ?, ville_a = ?, date_debut = ?, nb_jour = ?, date_fin = ?, user_id = ?, maps_link = ? WHERE id_annonce = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, annonce.getTitreA());
            preparedStatement.setString(2, annonce.getDescriptionA());
            preparedStatement.setString(3, annonce.getVilleA());
            preparedStatement.setDate(4, java.sql.Date.valueOf(annonce.getDateDebut()));
            preparedStatement.setString(5, annonce.getNbJour());
            preparedStatement.setDate(6, java.sql.Date.valueOf(annonce.getDateFin()));
            preparedStatement.setString(7, annonce.getUser());
            preparedStatement.setString(8, annonce.getMapsLink());
            preparedStatement.setString(9, annonce.getIdAnnonce());
            preparedStatement.executeUpdate();
            String mapsLink = generateMapsLink(annonce.getVilleA());
            annonce.setMapsLink(mapsLink);
        }
    }
    private String generateMapsLink(String city) {
        // Assume generating maps link based on city coordinates
        // You need to implement this according to your logic
        return "latitude,longitude"; // Example: "36.7783,119.4179"
    }

    // Méthode pour supprimer une annonce
    public void supprimer(Annonce annonce) throws SQLException {
        String sql = "DELETE FROM annonce WHERE id_annonce = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, annonce.getIdAnnonce());
            preparedStatement.executeUpdate();
        }
    }

    // Méthode pour afficher toutes les annonces
    public List<Annonce> afficher() throws SQLException {
        String sql = "SELECT * FROM annonce";
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
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
                annonce.setUser(rs.getString("user_id"));
                annonce.setMapsLink(rs.getString("maps_link"));
                annonces.add(annonce);
            }

            return annonces;
        }
    }
}
