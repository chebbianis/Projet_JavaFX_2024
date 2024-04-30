package services;

import models.Evenement;
import utils.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EvenementService implements IService<Evenement> {

    private Connection connection;

    public EvenementService() {
        connection = MyConnection.getInstance().getCnx();
    }

    // Méthode pour ajouter une evenement
    public void ajouter(Evenement evenement) throws SQLException {
        String sql = "INSERT INTO evenement (titre_e, description_e, ville_e, date_debut_e, nb_jour_e, date_fin_e) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, evenement.getTitreE());
        preparedStatement.setString(2, evenement.getDescriptionE());
        preparedStatement.setString(3, evenement.getVilleE());
        preparedStatement.setDate(4, java.sql.Date.valueOf(evenement.getDateDebutE()));
        preparedStatement.setString(5, evenement.getNbJourE());
        preparedStatement.setDate(6, java.sql.Date.valueOf(evenement.getDateFinE()));
        preparedStatement.executeUpdate();
    }

    // Méthode pour modifier une evenement
    public void modifier(Evenement evenement) throws SQLException {
        String sql = "UPDATE evenement SET titre_e = ?, description_e = ?, ville_e = ?, date_debut_e = ?, nb_jour_e = ?, date_fin_e = ? WHERE id_evenement = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, evenement.getTitreE());
        preparedStatement.setString(2, evenement.getDescriptionE());
        preparedStatement.setString(3, evenement.getVilleE());
        preparedStatement.setDate(4, java.sql.Date.valueOf(evenement.getDateDebutE()));
        preparedStatement.setString(5, evenement.getNbJourE());
        preparedStatement.setDate(6, java.sql.Date.valueOf(evenement.getDateFinE()));
            preparedStatement.setString(7, evenement.getIdEvenement());
        preparedStatement.executeUpdate();
    }

    // Méthode pour supprimer une evenement
    public void supprimer(Evenement evenement) throws SQLException {
        String sql = "DELETE FROM evenement WHERE id_evenement = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, evenement.getIdEvenement());
        preparedStatement.executeUpdate();
    }

    // Méthode pour afficher toutes les evenements
    public List<Evenement> afficher() throws SQLException {
        String sql = "SELECT * FROM evenement";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<Evenement> evenements = new ArrayList<>();

        while (rs.next()) {
            Evenement evenement = new Evenement();
            evenement.setIdEvenement(rs.getString("id_evenement"));
            evenement.setTitreE(rs.getString("titre_e"));
            evenement.setDescriptionE(rs.getString("description_e"));
            evenement.setVilleE(rs.getString("ville_e"));
            evenement.setDateDebutE(rs.getDate("date_debut_e").toLocalDate());
            evenement.setNbJourE(rs.getString("nb_jour_e"));
            evenement.setDateFinE(rs.getDate("date_fin_e").toLocalDate());
            evenements.add(evenement);
        }

        return evenements;
    }
}
