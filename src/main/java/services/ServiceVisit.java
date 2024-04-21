package services;

import entities.Maison;
import entities.Visit;
import utils.MyDB;
import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ServiceVisit implements IService <Visit> {
    private Connection connection;

    public ServiceVisit() {

        connection = MyDB.getInstance().getConnection();
    }

    @Override

    public void ajouter(Visit visit) throws SQLException {
        String req = "INSERT INTO visit (id, numero, nom, email, refB, date_visit) " +
                "VALUES ('" + visit.getId() + "', '" + visit.getNumero() + "', '" + visit.getNom() + "', '" + visit.getEmail() + "', '" + visit.getRefB() + "', '" + visit.getDateVisit() + "')";
        Statement ste = connection.createStatement();
        ste.executeUpdate(req);
    }
/*
    public void ajouter(Visit visit) throws SQLException {
        String req = "INSERT INTO visit (numero, nom, email, refB, date_visit) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setInt(1, visit.getNumero());
        preparedStatement.setString(2, visit.getNom());
        preparedStatement.setString(3, visit.getEmail());
        preparedStatement.setInt(4, visit.getRefB());
        preparedStatement.setDate(5, java.sql.Date.valueOf(visit.getDateVisit()));

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }
*/


    @Override
    public void modifier(Visit visit) throws SQLException {
        String req = "UPDATE visit SET numero=?, nom=?, email=?, refB=?, date_visit=? WHERE id=?";
        PreparedStatement pre = connection.prepareStatement(req);

        pre.setInt(1, visit.getNumero());
        pre.setString(2, visit.getNom());
        pre.setString(3, visit.getEmail());
        pre.setInt(4, visit.getRefB());
        pre.setObject(5, visit.getDateVisit()); // Use setObject for LocalDate
        pre.setInt(6, visit.getId());
        pre.executeUpdate();
    }


    @Override
    public void supprimer(Visit visit) throws SQLException {
        String req = "delete from visit where id=?";
        PreparedStatement pre = connection.prepareStatement(req);
        pre.setInt(1, visit.getId());
        pre.executeUpdate();
    }

    @Override
    public List<Visit> afficher() throws SQLException {
        String req = "SELECT * FROM visit ";
        PreparedStatement pre = connection.prepareStatement(req);
       // pre.setString(1, "visit");
        ResultSet resultSet = pre.executeQuery();

        List<Visit> visitList = new ArrayList<>();
        while (resultSet.next()) {
            Visit visit = new Visit();
            visit.setId(resultSet.getInt("id"));
            visit.setNumero(resultSet.getInt("numero"));
            visit.setNom(resultSet.getString("nom"));
            visit.setEmail(resultSet.getString("email"));
            visit.setRefB(resultSet.getInt("refB"));
            visit.setDateVisit(resultSet.getDate("date_visit").toLocalDate());
            visitList.add(visit);
        }
        return visitList;
    }
    public List<Visit> rechercher(String rechercheText) throws SQLException {
        String sql = "SELECT * FROM visit WHERE id LIKE ? OR numero LIKE ? OR nom LIKE ? OR email LIKE ? OR refB LIKE ? OR date_visit LIKE ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, "%" + rechercheText + "%");
        preparedStatement.setString(2, "%" + rechercheText + "%");
        preparedStatement.setString(3, "%" + rechercheText + "%");
        preparedStatement.setString(4, "%" + rechercheText + "%");
        preparedStatement.setString(5, "%" + rechercheText + "%");
        preparedStatement.setString(6, "%" + rechercheText + "%");

        ResultSet rs = preparedStatement.executeQuery();

        List<Visit> visits = new ArrayList<>();
        while (rs.next()) {
            Visit visit = new Visit();
            visit.setId(rs.getInt("id"));
            visit.setNumero(rs.getInt("numero"));
            visit.setNom(rs.getString("nom"));
            visit.setEmail(rs.getString("email"));
            visit.setRefB(rs.getInt("refB"));
            visit.setDateVisit(LocalDate.parse(rs.getString("date_visit")));
            visits.add(visit);
        }
        return visits;
    }
}