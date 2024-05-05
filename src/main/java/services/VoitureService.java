package services;

import models.Voiture;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VoitureService implements IService<Voiture>{

    private Connection connection;

    public VoitureService() {
        connection = MyDatabase.getInstance().getConnection();
    }
    @Override
    public int add(Voiture voiture) throws SQLException {

        String sql = "INSERT INTO voiture (marque, annee, prix_j, kilometrage, nbrPlaces) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, voiture.getMarque());
        statement.setDate(2, java.sql.Date.valueOf(voiture.getAnnee()));
        statement.setInt(3, voiture.getPrix_j());
        statement.setInt(4, voiture.getKilometrage());
        statement.setInt(5, voiture.getNbrPlaces());
        statement.executeUpdate();

        // Retrieve the auto-generated ID
        ResultSet generatedKeys = statement.getGeneratedKeys();
        int voitureId = -1; // Default value if no key is generated
        if (generatedKeys.next()) {
            voitureId = generatedKeys.getInt(1); // Assuming the ID is in the first column
        }

        return voitureId;
    }

    public void modifier(Voiture voiture) throws SQLException {

        String sql = "update voiture set marque = ?, annee = ?, prix_j= ? , kilometrage = ?, nbrPlaces= ? where id_voiture = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, voiture.getMarque());
        preparedStatement.setDate(2, java.sql.Date.valueOf(voiture.getAnnee()));
        preparedStatement.setInt(3, voiture.getPrix_j());
        preparedStatement.setInt(4, voiture.getKilometrage());
        preparedStatement.setInt(5, voiture.getNbrPlaces());
        preparedStatement.setInt(6,voiture.getId_voiture());
        preparedStatement.executeUpdate();

    }

    @Override
    public void supprimer(int id_voiture) throws SQLException {

        String sql = "delete from voiture where id_voiture = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id_voiture);
        preparedStatement.executeUpdate();

    }

    @Override
    public List<Voiture> recuperer() throws SQLException {
        List<Voiture> voitures = new ArrayList<>();
        String sql = "Select * from voiture";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()){
            Voiture v = new Voiture();
            v.setId_voiture(rs.getInt("id_voiture"));
            v.setMarque(rs.getString("marque"));
            v.setAnnee(rs.getDate("annee").toLocalDate());
            v.setPrix_j(rs.getInt("prix_j"));
            v.setKilometrage(rs.getInt("kilometrage"));
            v.setNbrPlaces(rs.getInt("nbrPlaces"));
            voitures.add(v);
        }
        return voitures;
    }

    public int getPrixJById(int voitureId) throws SQLException {
        String sql = "SELECT prix_j FROM voiture WHERE id_voiture = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, voitureId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("prix_j");
                } else {
                    throw new SQLException("Voiture with ID " + voitureId + " not found.");
                }
            }
        }
    }
    public int getVoitureIdByMarque(String marque) throws SQLException {
        String sql = "SELECT id_voiture FROM voiture WHERE marque = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, marque);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id_voiture");
                }
            }
        }
        throw new SQLException("No voiture found with marque: " + marque);
    }



}
