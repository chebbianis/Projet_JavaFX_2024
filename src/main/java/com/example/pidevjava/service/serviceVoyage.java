package com.example.pidevjava.service;
import com.example.pidevjava.Entities.Voyage;
import com.example.pidevjava.DataBase.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class serviceVoyage implements iservice<Voyage> {


    private Connection connection;

    public serviceVoyage() {
        connection = MyConnection.getInstance().getCnx();
    }
    public void ServiceVoyage() {
        connection = MyConnection.getInstance().getCnx();
    }

    // Méthode pour ajouter un voyage
    public void ajouter(Voyage voyage) throws SQLException {
        String sql = "INSERT INTO voyage (programme, date_depart, date_arrive, prix, destination) " +
                "VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, voyage.getProgramme());
        preparedStatement.setDate(2, java.sql.Date.valueOf(voyage.getDateDepart()));
        preparedStatement.setDate(3, java.sql.Date.valueOf(voyage.getDateArrive()));
        preparedStatement.setString(4, voyage.getPrix());
        preparedStatement.setString(5, voyage.getDestination());
        preparedStatement.executeUpdate();
    }

    // Méthode pour modifier un voyage
    public void modifier(Voyage voyage) throws SQLException {
        String sql = "UPDATE voyage SET programme = ?, date_depart = ?, date_arrive = ?, prix = ?, destination = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, voyage.getProgramme());
        preparedStatement.setDate(2, java.sql.Date.valueOf(voyage.getDateDepart()));
        preparedStatement.setDate(3, java.sql.Date.valueOf(voyage.getDateArrive()));
        preparedStatement.setString(4, voyage.getPrix());
        preparedStatement.setString(5, voyage.getDestination());
        preparedStatement.setInt(6, voyage.getId());
        preparedStatement.executeUpdate();
    }

    // Méthode pour supprimer un voyage
    public void supprimer(Voyage voyage) throws SQLException {
        String sql = "DELETE FROM voyage WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, voyage.getId());
        preparedStatement.executeUpdate();
    }

    // Méthode pour afficher tous les voyages
    public List<Voyage> afficher() throws SQLException {
        String sql = "SELECT * FROM voyage";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<Voyage> voyages = new ArrayList<>();
        while (rs.next()) {
            Voyage voyage = new Voyage();
            voyage.setId(rs.getInt("id"));
            voyage.setProgramme(rs.getString("programme"));
            voyage.setDateDepart(rs.getDate("date_depart").toLocalDate());
            voyage.setDateArrive(rs.getDate("date_arrive").toLocalDate());
            voyage.setPrix(rs.getString("prix"));
            voyage.setDestination(rs.getString("destination"));
            voyages.add(voyage);
        }
        return voyages;

    }
}


