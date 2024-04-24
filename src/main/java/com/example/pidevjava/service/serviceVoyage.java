package com.example.pidevjava.service;
import com.example.pidevjava.Entities.Voyage;
import com.example.pidevjava.DataBase.MyConnection;
import com.example.pidevjava.Entities.Voyageur;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String sql = "SELECT v.id, v.programme, v.date_depart, v.date_arrive, v.prix, v.destination, " +
                "vr.id AS voyageur_id, vr.num_pass, vr.nom, vr.prenom, vr.age, vr.etat_civil, vr.email " +
                "FROM voyage v LEFT JOIN voyageur vr ON v.id = vr.voyage_id";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<Voyage> voyages = new ArrayList<>();
        Map<Integer, Voyage> voyageMap = new HashMap<>(); // Pour éviter de créer des doublons de voyages

        while (rs.next()) {
            int voyageId = rs.getInt("id");
            if (!voyageMap.containsKey(voyageId)) {
                Voyage voyage = new Voyage();
                voyage.setId(voyageId);
                voyage.setProgramme(rs.getString("programme"));
                voyage.setDateDepart(rs.getDate("date_depart").toLocalDate());
                voyage.setDateArrive(rs.getDate("date_arrive").toLocalDate());
                voyage.setPrix(rs.getString("prix"));
                voyage.setDestination(rs.getString("destination"));
                voyage.setVoyageurs(new ArrayList<>());
                voyageMap.put(voyageId, voyage);
            }

            Voyageur voyageur = new Voyageur();
            voyageur.setId(rs.getInt("voyageur_id"));
            voyageur.setNum_pass(rs.getInt("num_pass"));
            voyageur.setNom(rs.getString("nom"));
            voyageur.setPrenom(rs.getString("prenom"));
            voyageur.setAge(rs.getInt("age"));
            voyageur.setEtat_civil(rs.getString("etat_civil"));
            voyageur.setEmail(rs.getString("email"));

            voyageMap.get(voyageId).getVoyageurs().add(voyageur);
        }

        voyages.addAll(voyageMap.values());
        return voyages;
    }

}


