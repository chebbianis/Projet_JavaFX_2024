package services;

import entities.Maison;
import utils.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ServiceMaison implements IService <Maison> {
    private Connection connection;
    public ServiceMaison(){
        connection= MyDB.getInstance().getConnection();
    }
   /* @Override
    public void ajouter(Maison maison) throws SQLException {
        String req = "INSERT INTO maison (nom, adresse, nombre_chambre, prix, type,image) " +
                "VALUES ('" + maison.getNom() + "', '" + maison.getAdresse() + "', '" + maison.getNombreChambre() + "', '" + maison.getPrix() + "', '" + maison.getType() + "', '" + maison.getImage() + "')";
        Statement ste =connection.createStatement();
        ste.executeUpdate(req);
    }
    */
   @Override
   public void ajouter(Maison maison) throws SQLException {
       if (maisonExiste(maison.getNom())) {
           throw new SQLException("Ce nom de maison existe déjà.");
       }
       String req = "INSERT INTO maison (nom, adresse, nombre_chambre, prix, type, image) VALUES (?, ?, ?, ?, ?, ?)";
       PreparedStatement pre = connection.prepareStatement(req);
       pre.setString(1, maison.getNom());
       pre.setString(2, maison.getAdresse());
       pre.setInt(3, maison.getNombreChambre());
       pre.setInt(4, maison.getPrix());
       pre.setString(5, maison.getType());
       pre.setString(6, maison.getImage());
       pre.executeUpdate();
   }
    public boolean maisonExiste(String nom) throws SQLException {
        String req = "SELECT COUNT(*) FROM maison WHERE nom = ?";
        PreparedStatement pre = connection.prepareStatement(req);
        pre.setString(1, nom);
        ResultSet resultSet = pre.executeQuery();
        if (resultSet.next()) {
            int count = resultSet.getInt(1);
            return count > 0;
        }
        return false;
    }

    @Override
    public void modifier(Maison maison) throws SQLException {
        String req = "UPDATE maison SET nom=?, adresse=?, nombre_chambre=?, prix=?, type=?, image=? WHERE ref_b=?";
        PreparedStatement pre = connection.prepareStatement(req);
        pre.setString(1, maison.getNom());
        pre.setString(2, maison.getAdresse());
        pre.setInt(3, maison.getNombreChambre());
        pre.setInt(4, maison.getPrix());
        pre.setString(5, maison.getType());
        pre.setString(6, maison.getImage()); // Use setString for String values
        pre.setInt(7, maison.getRefB());
        pre.executeUpdate();
    }


    @Override
    public void supprimer(Maison maison) throws SQLException {
        String req ="delete from maison where ref_b=?";
        PreparedStatement pre=connection.prepareStatement(req);
        pre.setInt(1,maison.getRefB());
        pre.executeUpdate();
    }

    @Override
    public List<Maison> afficher() throws SQLException {
       // return null;
        String req = "SELECT * FROM maison ";
        PreparedStatement pre = connection.prepareStatement(req);
        //pre.setString(1, "Maison");
        ResultSet resultSet = pre.executeQuery();

        List<Maison> maisonList = new ArrayList<>();
        while (resultSet.next()) {
            Maison maison = new Maison();
            maison.setRefB(resultSet.getInt("ref_b"));
            maison.setNom(resultSet.getString("nom"));
            maison.setAdresse(resultSet.getString("adresse"));
            maison.setNombreChambre(resultSet.getInt("nombre_chambre"));
            maison.setPrix(resultSet.getInt("prix"));
            maison.setType(resultSet.getString("type"));
            maison.setImage(resultSet.getString("image"));
            maisonList.add(maison);
        }
        return maisonList;
    }
    public List<Maison> rechercher(String rechercheText) throws SQLException {
        String sql = "SELECT * FROM maison WHERE ref_b LIKE ? OR nom LIKE ? OR adresse LIKE ? OR nombre_chambre LIKE ? OR prix LIKE ? OR type LIKE ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, "%" + rechercheText + "%");
        preparedStatement.setString(2, "%" + rechercheText + "%");
        preparedStatement.setString(3, "%" + rechercheText + "%");
        preparedStatement.setString(4, "%" + rechercheText + "%");
        preparedStatement.setString(5, "%" + rechercheText + "%");
        preparedStatement.setString(6, "%" + rechercheText + "%");

        ResultSet rs = preparedStatement.executeQuery();

        List<Maison> maisons = new ArrayList<>();
        while (rs.next()) {
            Maison maison = new Maison();
            maison.setRefB(rs.getInt("ref_b"));
            maison.setNom(rs.getString("nom"));
            maison.setAdresse(rs.getString("adresse"));
            maison.setNombreChambre(rs.getInt("nombre_chambre"));
            maison.setPrix(rs.getInt("prix"));
            maison.setType(rs.getString("type"));
            maison.setImage(rs.getString("image"));
            maisons.add(maison);
        }
        return maisons;
    }


}
