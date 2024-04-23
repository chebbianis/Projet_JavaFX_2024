package services;

import entities.Reservation;
import utils.MySQLConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceReservation implements IServices<Reservation> {

    private static Connection connection;

    public ServiceReservation() {
        connection = MySQLConnector.getInstance().getConnection();
    }

  @Override
    public void ajouter(Reservation reservation) throws SQLException {
        String sql = "INSERT INTO reservation (id_r, id_client, date_arrivee, date_depart, type_r, prix_total, idH, nbre_chambre, type_chambre, nbre_adulte, nbre_enfant) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setInt(1, reservation.getIdR());
        preparedStatement.setInt(2, reservation.getId_client());
        preparedStatement.setDate(3, Date.valueOf(reservation.getDate_arrivee()));
        preparedStatement.setDate(4, Date.valueOf(reservation.getDate_depart()));
        preparedStatement.setString(5, reservation.getType_r());
        preparedStatement.setInt(6, reservation.getPrix_total());
        preparedStatement.setInt(7, reservation.getIdH());
        preparedStatement.setInt(8, reservation.getNbre_chambre());
        preparedStatement.setString(9, reservation.getType_chambre());
        preparedStatement.setInt(10, reservation.getNbre_adulte());
        preparedStatement.setInt(11, reservation.getNbre_enfant());

        preparedStatement.executeUpdate();
    }

    @Override
    public void modifier(Reservation reservation) throws SQLException {
        String sql = "UPDATE reservation SET id_client = ?, date_arrivee = ?, date_depart = ?, type_r = ?, prix_total = ?, idH = ?, nbre_chambre = ?, type_chambre = ?, nbre_adulte = ?, nbre_enfant = ? WHERE id_r = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, reservation.getId_client());
        preparedStatement.setDate(2, Date.valueOf(reservation.getDate_arrivee()));
        preparedStatement.setDate(3, Date.valueOf(reservation.getDate_depart()));
        preparedStatement.setString(4, reservation.getType_r());
        preparedStatement.setInt(5, reservation.getPrix_total());
        preparedStatement.setInt(6, reservation.getIdH());
        preparedStatement.setInt(7, reservation.getNbre_chambre());
        preparedStatement.setString(8, reservation.getType_chambre());
        preparedStatement.setInt(9, reservation.getNbre_adulte());
        preparedStatement.setInt(10, reservation.getNbre_enfant());
        preparedStatement.setInt(11, reservation.getIdR());

        preparedStatement.executeUpdate();
    }

    @Override
    public void supprimer(Reservation reservation) throws SQLException {
        String sql = "DELETE FROM reservation WHERE id_r = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, reservation.getIdR());
        preparedStatement.executeUpdate();
    }


    @Override
    public List<Reservation> afficher() throws SQLException {
        String sql = "SELECT id_r, id_client, date_arrivee, date_depart, type_r, prix_total, idH, nbre_chambre, type_chambre, nbre_adulte, nbre_enfant FROM reservation ORDER BY id_r DESC";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<Reservation> reservations = new ArrayList<>();
        while (rs.next()) {
            Reservation reservation = new Reservation();
            reservation.setIdR(rs.getInt("id_r"));
            reservation.setId_client(rs.getInt("id_client"));
            reservation.setDate_arrivee(rs.getDate("date_arrivee").toLocalDate());
            reservation.setDate_depart(rs.getDate("date_depart").toLocalDate());
            reservation.setType_r(rs.getString("type_r"));
            reservation.setPrix_total(rs.getInt("prix_total"));
            reservation.setIdH(rs.getInt("idH"));
            reservation.setNbre_chambre(rs.getInt("nbre_chambre"));
            reservation.setType_chambre(rs.getString("type_chambre"));
            reservation.setNbre_adulte(rs.getInt("nbre_adulte"));
            reservation.setNbre_enfant(rs.getInt("nbre_enfant"));
            reservations.add(reservation);
        }
        return reservations;
    }

}
