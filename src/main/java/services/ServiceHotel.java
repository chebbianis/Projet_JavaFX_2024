package services;
import entities.Hotel;
import utils.MySQLConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;

public class ServiceHotel implements IServices<Hotel> {

    private Connection connection;

    public ServiceHotel() {
        connection = MySQLConnector.getInstance().getConnection();
    }

    @Override
    public void ajouter(Hotel hotel) throws SQLException {
        String req = "INSERT INTO hotel ( nom_hotel, nbre_etoile, adresse_hotel, prix_nuit,image) " +
                "VALUES ('" + hotel.getNom_hotel() + "', '" + hotel.getNbre_etoile() + "', '" + hotel.getAdresse_hotel() + "', '" + hotel.getPrix_nuit() + "','"+hotel.getImage()+"')";
        Statement ste =connection.createStatement();
        ste.executeUpdate(req);
    }

    @Override
    public void modifier(Hotel hotel) throws SQLException {
        String req = "UPDATE hotel SET nom_hotel=?, nbre_etoile=? ,adresse_hotel=?,prix_nuit=? ,image=?  WHERE id_h=? ";
        PreparedStatement pre = connection.prepareStatement(req);
        pre.setString(1, hotel.getNom_hotel());
        pre.setInt(2, hotel.getNbre_etoile());
        pre.setString(3, hotel.getAdresse_hotel());
        pre.setFloat(4, hotel.getPrix_nuit());
        pre.setString(5, hotel.getImage());
        pre.setInt(6, hotel.getIdH());
        pre.executeUpdate();

    }



    @Override
    public void supprimer(Hotel hotel) throws SQLException {
        String sql = "DELETE FROM hotel WHERE id_h = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, hotel.getIdH());
        preparedStatement.executeUpdate();
    }


    @Override
    public List<Hotel> afficher() throws SQLException {
        String sql = "SELECT * FROM hotel";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<Hotel> hotels = new ArrayList<>();
        while (rs.next()) {
            Hotel hotel = new Hotel();
            hotel.setIdH(rs.getInt("id_h"));
            hotel.setNom_hotel(rs.getString("nom_hotel"));
            hotel.setNbre_etoile(rs.getInt("nbre_etoile"));
            hotel.setAdresse_hotel(rs.getString("adresse_hotel"));
            hotel.setPrix_nuit(rs.getFloat("prix_nuit"));
            hotel.setImage(rs.getString("image"));
            hotels.add(hotel);
        }
        return hotels;
    }



    public List<Hotel> rechercher(String rechercheText) throws SQLException {
        String sql = "SELECT * FROM hotel WHERE nom_hotel LIKE ? OR adresse_hotel LIKE ? OR nbre_etoile = ? OR prix_nuit LIKE ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, "%" + rechercheText + "%"); // nom_hotel LIKE ?
        preparedStatement.setString(2, "%" + rechercheText + "%"); // adresse_hotel LIKE ?
        try {
            int nbreEtoile = Integer.parseInt(rechercheText);
            preparedStatement.setInt(3, nbreEtoile); // nbre_etoile = ?
        } catch (NumberFormatException e) {
            preparedStatement.setInt(3, -1); // Utiliser -1 pour indiquer une recherche sans filtre d'étoiles
        }

        preparedStatement.setString(4, "%" + rechercheText + "%"); // prix_nuit LIKE ?

        ResultSet rs = preparedStatement.executeQuery();

        List<Hotel> hotels = new ArrayList<>();
        while (rs.next()) {
            Hotel hotel = new Hotel();
            hotel.setIdH(rs.getInt("id_h"));
            hotel.setNom_hotel(rs.getString("nom_hotel"));
            hotel.setNbre_etoile(rs.getInt("nbre_etoile"));
            hotel.setAdresse_hotel(rs.getString("adresse_hotel"));
            hotel.setPrix_nuit(rs.getFloat("prix_nuit"));
            hotel.setImage(rs.getString("image"));
            hotels.add(hotel);
        }
        return hotels;
    }

}