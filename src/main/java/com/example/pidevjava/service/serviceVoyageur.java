package com.example.pidevjava.service;

import com.example.pidevjava.Entities.Voyageur;
import com.example.pidevjava.DataBase.MyConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class serviceVoyageur implements iservice<Voyageur> {
    private Connection connection;

    public serviceVoyageur() {
        connection = MyConnection.getInstance().getCnx();
    }

    @Override
    public void ajouter(Voyageur voyageur) throws SQLException {
        String sql = "INSERT INTO voyageur (num_pass, nom, prenom, age, etat_civil, email) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, voyageur.getNum_pass());
        preparedStatement.setString(2, voyageur.getNom());
        preparedStatement.setString(3, voyageur.getPrenom());
        preparedStatement.setInt(4, voyageur.getAge());
        preparedStatement.setString(5, voyageur.getEtat_civil());
        preparedStatement.setString(6, voyageur.getEmail());
        preparedStatement.executeUpdate();
    }

    @Override
    public void modifier(Voyageur voyageur) throws SQLException {
        // Implémentez la logique de modification ici
        String sql = "UPDATE voyageur SET nom = ?, prenom = ?, age = ?, etat_civil = ?, email = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, voyageur.getNom());
        preparedStatement.setString(2, voyageur.getPrenom());
        preparedStatement.setInt(3, voyageur.getAge());
        preparedStatement.setString(4, voyageur.getEtat_civil());
        preparedStatement.setString(5, voyageur.getEmail());
        preparedStatement.setInt(6, voyageur.getId());
        preparedStatement.executeUpdate();
    }

    @Override
    public void supprimer(Voyageur voyageur) throws SQLException {
        // Implémentez la logique de suppression ici
        String sql = "DELETE FROM voyageur WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, voyageur.getId());
        preparedStatement.executeUpdate();
    }

    @Override
    public List<Voyageur> afficher() throws SQLException {
        // Implémentez la logique pour afficher les voyageurs
        String sql = "SELECT * FROM voyageur";  // Sélectionnez tous les voyageurs

        Connection connection = MyConnection.getInstance().getCnx();  // Obtenez la connexion à la base de données
        List<Voyageur> voyageurs = new ArrayList<>();  // Liste pour stocker les voyageurs récupérés

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            // Parcourez le résultat de la requête
            while (rs.next()) {
                // Créez un nouvel objet Voyageur avec les données de la base de données
                Voyageur voyageur = new Voyageur(
                        rs.getInt("num_pass"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getInt("age"),
                        rs.getString("etat_civil"),
                        rs.getString("email")
                );
                voyageur.setId(rs.getInt("id"));  // Définissez l'ID du voyageur
                voyageurs.add(voyageur);  // Ajoutez le voyageur à la liste
            }
        } catch (SQLException e) {
            // Gérez les exceptions ou lancez-les à l'appelant
            throw new SQLException("Erreur lors de la récupération des voyageurs: " + e.getMessage());
        }

        return voyageurs;  // Renvoie la liste des voyageurs récupérés
    }
    public List<Voyageur> rechercher(String rechercheText) throws SQLException {
        String sql = "SELECT * FROM voyageur WHERE num_pass = ? OR nom LIKE ? OR prenom LIKE ? OR age = ? OR etat_civil LIKE ? OR email LIKE ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,  rechercheText );
        preparedStatement.setString(2, "%" + rechercheText + "%");
        preparedStatement.setString(3, "%" + rechercheText + "%");
        preparedStatement.setString(4,  rechercheText );
        preparedStatement.setString(5, "%" + rechercheText + "%");
        preparedStatement.setString(6, "%" + rechercheText + "%");

        ResultSet rs = preparedStatement.executeQuery();

        List<Voyageur> Voyageurs = new ArrayList<>();
        while (rs.next()) {
            Voyageur Voyageur = new Voyageur();
            Voyageur.setId(rs.getInt("id"));
            Voyageur.setNom(rs.getString("nom"));
            Voyageur.setPrenom(rs.getString("prenom"));
           Voyageur.setAge(rs.getInt("age"));
            Voyageur.setEtat_civil(rs.getString("etat_civil"));
            Voyageur.setEmail(rs.getString("email"));
            Voyageurs.add(Voyageur);
        }
        return Voyageurs;
    }
    }

