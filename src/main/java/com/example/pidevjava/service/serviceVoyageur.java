package com.example.pidevjava.service;

import com.example.pidevjava.Entities.Voyageur;
import com.example.pidevjava.DataBase.MyConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class serviceVoyageur implements iservice<Voyageur> {
    private Connection connection;

    public serviceVoyageur() {
        connection = MyConnection.getInstance().getCnx();
    }

    @Override
    public void ajouter(Voyageur voyageur) throws SQLException {
        String sql = "INSERT INTO voyageur (num_pass, nom, prenom, age, etat_civil, email, date_nais) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, voyageur.getNum_pass());
        preparedStatement.setString(2, voyageur.getNom());
        preparedStatement.setString(3, voyageur.getPrenom());
        preparedStatement.setInt(4, voyageur.getAge());
        preparedStatement.setString(5, voyageur.getEtat_civil());
        preparedStatement.setString(6, voyageur.getEmail());
        preparedStatement.setObject(7, voyageur.getDate_nais());
        preparedStatement.executeUpdate();
    }

    @Override
    public void modifier(Voyageur voyageur) throws SQLException {
        String sql = "UPDATE voyageur SET nom = ?, prenom = ?, age = ?, etat_civil = ?, email = ?, date_nais = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, voyageur.getNom());
        preparedStatement.setString(2, voyageur.getPrenom());
        preparedStatement.setInt(3, voyageur.getAge());
        preparedStatement.setString(4, voyageur.getEtat_civil());
        preparedStatement.setString(5, voyageur.getEmail());
        preparedStatement.setObject(6, voyageur.getDate_nais());
        preparedStatement.setInt(7, voyageur.getId());
        preparedStatement.executeUpdate();
    }

    @Override
    public void supprimer(Voyageur voyageur) throws SQLException {
        String sql = "DELETE FROM voyageur WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, voyageur.getId());
        preparedStatement.executeUpdate();
    }

    public List<Voyageur> afficher() throws SQLException {
        String sql = "SELECT * FROM voyageur";
        List<Voyageur> voyageurs = new ArrayList<>();

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                LocalDate dateNaissance = null;
                Date dateNaissanceSql = rs.getDate("date_nais");
                if (dateNaissanceSql != null) {
                    dateNaissance = dateNaissanceSql.toLocalDate();
                }
                Voyageur voyageur = new Voyageur(
                        rs.getInt("id"),
                        rs.getInt("num_pass"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getInt("age"),
                        rs.getString("etat_civil"),
                        rs.getString("email"),
                        dateNaissance
                );
                voyageurs.add(voyageur);
            }
        }

        return voyageurs;

    }

    public List<Voyageur> rechercher(String rechercheText) throws SQLException {
        String sql = "SELECT * FROM voyageur WHERE num_pass = ? OR nom LIKE ? OR prenom LIKE ? OR age = ? OR etat_civil LIKE ? OR email LIKE ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, rechercheText);
        preparedStatement.setString(2, "%" + rechercheText + "%");
        preparedStatement.setString(3, "%" + rechercheText + "%");
        preparedStatement.setString(4, rechercheText);
        preparedStatement.setString(5, "%" + rechercheText + "%");
        preparedStatement.setString(6, "%" + rechercheText + "%");

        ResultSet rs = preparedStatement.executeQuery();

        List<Voyageur> voyageurs = new ArrayList<>();
        while (rs.next()) {
            Voyageur voyageur = new Voyageur(
                    rs.getInt("num_pass"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getInt("age"),
                    rs.getString("etat_civil"),
                    rs.getString("email"),
                    rs.getDate("date_nais").toLocalDate()
            );
            voyageur.setId(rs.getInt("id"));
            voyageurs.add(voyageur);
        }
        return voyageurs;
    }

    public List<Voyageur> rechercherAll() throws SQLException {
        String sql = "SELECT * FROM voyageur";
        List<Voyageur> voyageurs = new ArrayList<>();

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                Voyageur voyageur = new Voyageur(
                        rs.getInt("num_pass"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getInt("age"),
                        rs.getString("etat_civil"),
                        rs.getString("email"),
                        rs.getDate("date_nais").toLocalDate()
                );
                voyageur.setId(rs.getInt("id"));
                voyageurs.add(voyageur);
            }
        }

        return voyageurs;
    }
}
