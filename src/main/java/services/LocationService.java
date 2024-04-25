package services;

import models.Location;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocationService implements IService<Location>{

    private Connection connection;

    public LocationService() {
        connection = MyDatabase.getInstance().getConnection();
    }
    @Override
    public int add(Location location) throws SQLException {

        String sql = "INSERT INTO location (date_debut, date_fin, user_id, voiture_id, typePaiement, options_supp) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        statement.setDate(1, java.sql.Date.valueOf(location.getDate_debut()));
        statement.setDate(2, java.sql.Date.valueOf(location.getDate_fin()));
        statement.setInt(3, location.getUser_id());
        statement.setInt(4, location.getVoiture_id());
        statement.setString(5, location.getTypePaiement());
        statement.setString(6, location.getOptions_supp());
        statement.executeUpdate();

        // Retrieve the auto-generated ID
        ResultSet generatedKeys = statement.getGeneratedKeys();
        int locationId = -1; // Default value if no key is generated
        if (generatedKeys.next()) {
            locationId = generatedKeys.getInt(1); // Assuming the ID is in the first column
        }

        return locationId;
    }

    public void modifier(Location location) throws SQLException {

        String sql = "update location set date_debut = ?, date_fin = ?, user_id= ? , voiture_id = ?, typePaiement= ?, options_supp = ? where id_location = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setDate(1, java.sql.Date.valueOf(location.getDate_debut()));
        preparedStatement.setDate(2, java.sql.Date.valueOf(location.getDate_fin()));
        preparedStatement.setInt(3, location.getUser_id());
        preparedStatement.setInt(4, location.getVoiture_id());
        preparedStatement.setString(5, location.getTypePaiement());
        preparedStatement.setString(6, location.getOptions_supp());
        preparedStatement.setInt(7, location.getId_location());
        preparedStatement.executeUpdate();

    }

    @Override
    public void supprimer(int id_location) throws SQLException {

        String sql = "delete from location where id_location = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id_location);
        preparedStatement.executeUpdate();

    }

    @Override
    public List<Location> recuperer() throws SQLException {
        List<Location> locations = new ArrayList<>();
        String sql = "Select * from location";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()){
            Location v = new Location();
            v.setId_location(rs.getInt("id_location"));
            v.setDate_debut(rs.getDate("date_debut").toLocalDate());
            v.setDate_fin(rs.getDate("date_fin").toLocalDate());
            v.setUser_id(rs.getInt("user_id"));
            v.setVoiture_id(rs.getInt("voiture_id"));
            v.setTypePaiement(rs.getString("typePaiement"));
            v.setOptions_supp(rs.getString("options_supp"));
            locations.add(v);
        }
        return locations;
    }


}

