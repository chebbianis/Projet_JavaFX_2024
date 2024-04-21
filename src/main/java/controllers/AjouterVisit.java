package controllers;

import entities.Maison;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import entities.Visit;
import services.ServiceMaison;
import services.ServiceVisit;
import utils.MyDB;

import java.sql.*;
import java.time.LocalDate;

public class AjouterVisit {

    @FXML
    private ComboBox<String> comboV;

    @FXML
    private DatePicker dateV;

    @FXML
    private TextField emailV;

    @FXML
    private TextField nomV;

    @FXML
    private TextField numV;
    private Connection connection;
    private Visit visit;

   @FXML
  /*  void ajouterVisit(ActionEvent event) throws SQLException {
        try {
            if (nomV.getText().isEmpty() || emailV.getText().isEmpty() || dateV.getValue() == null) {
                // Afficher une alerte si un champ obligatoire est vide
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez remplir tous les champs.");
                alert.showAndWait();
            } else {
                int numero = Integer.parseInt(numV.getText());
                LocalDate DateRe = dateV.getValue();
                String selectedNomMaison = (String) comboV.getSelectionModel().getSelectedItem();
                int ResID = getNomMaison(selectedNomMaison);
                // String selectedNomUser = comboV.getSelectionModel().getSelectedItem();
                //    int idUser = getResIDFromNomUser(connect, selectedNomUser);

                // Utilisation de requêtes préparées
                String req = "INSERT INTO visit (numero, nom, email, refB, date_visit) VALUES (?, ?, ?, ?, ?)";
                this.connection = MyDB.getInstance().getConnection();
                PreparedStatement preparedStatement ;
                try {
                    preparedStatement = connection.prepareStatement(req);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                preparedStatement.setInt(1, visit.getNumero());
                preparedStatement.setString(2, visit.getNom());
                preparedStatement.setString(3, visit.getEmail());
                preparedStatement.setInt(4, visit.getRefB());
                preparedStatement.setDate(5, Date.valueOf(visit.getDateVisit()));
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    // Afficher une alerte de succès si l'ajout est réussi
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Succès");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("La demande a été ajoutée avec succès.");

                }
            }

        } catch (NumberFormatException e) {
            // Afficher une alerte si une valeur numérique est invalide
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez entrer des valeurs numériques valides.");
            alert.showAndWait();
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }*/
   private boolean verifierDisponibilite(LocalDate dateVisit, int refB) {
       try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tunvista", "root", "");
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) AS nombreVisites FROM visit WHERE date_visit = ? AND refB = ?")) {
           preparedStatement.setDate(1, java.sql.Date.valueOf(dateVisit));
           preparedStatement.setInt(2, refB);
           try (ResultSet resultSet = preparedStatement.executeQuery()) {
               if (resultSet.next()) {
                   int nombreVisites = resultSet.getInt("nombreVisites");
                   return nombreVisites < 5;
               }
           }
       } catch (SQLException e) {
           System.out.println("SQL Error: " + e.getMessage());
       }
       return false;
   }
   public void ajouterVisit(ActionEvent event) throws SQLException {

       try {
           if (nomV.getText().isEmpty() || emailV.getText().isEmpty() || dateV.getValue() == null) {
               Alert alert = new Alert(Alert.AlertType.ERROR);
               alert.setTitle("Erreur");
               alert.setHeaderText(null);
               alert.setContentText("Veuillez remplir tous les champs.");
               alert.showAndWait();
           } else {
               int numero = Integer.parseInt(numV.getText());
               LocalDate date_visit = dateV.getValue();
               String selectedNomMaison = comboV.getSelectionModel().getSelectedItem();
               int ref_b = getNomMaison(selectedNomMaison);
               if (!verifierDisponibilite(date_visit, ref_b)) {
                   Alert alert = new Alert(Alert.AlertType.ERROR);
                   alert.setTitle("Erreur");
                   alert.setHeaderText(null);
                   alert.setContentText("Le maison sélectionnée est indisponible , veuillez choisir une autre date .");
                   alert.showAndWait();
                   return;  }
               LocalDate dateVisit = dateV.getValue();
               LocalDate today = LocalDate.now();
               if (dateVisit.isBefore(today) ) {
                   Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                   successAlert.setTitle("Wrong date");
                   successAlert.setHeaderText("Please select dates from today onwards");
                   successAlert.showAndWait();
                   return;
               }
               String adresseEmailValue = emailV.getText();
               if (!adresseEmailValue.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
                   Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                   errorAlert.setTitle("Entrée invalide");
                   errorAlert.setHeaderText(null);
                   errorAlert.setContentText("Veuillez saisir une adresse e-mail valide.");
                   errorAlert.showAndWait();
                   return;
               }
               String numeroValue = Integer.toString(numero);
               if (numeroValue.length() > 8) {
                   Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                   errorAlert.setTitle("Entrée invalide");
                   errorAlert.setHeaderText(null);
                   errorAlert.setContentText("Le champ 'Numéro' ne doit pas dépasser 8 chiffres.");
                   errorAlert.showAndWait();
                   return;
               }

               Visit visit = new Visit();
               visit.setNumero(numero);
               visit.setNom(nomV.getText());
               visit.setEmail(emailV.getText());
               visit.setRefB(ref_b);
               visit.setDateVisit(date_visit);

               String req = "INSERT INTO visit (numero, nom, email, refB, date_visit) VALUES (?, ?, ?, ?, ?)";
               try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tunvista", "root", "");
                    PreparedStatement preparedStatement = connection.prepareStatement(req)) {
                   preparedStatement.setInt(1, visit.getNumero());
                   preparedStatement.setString(2, visit.getNom());
                   preparedStatement.setString(3, visit.getEmail());
                   preparedStatement.setInt(4, visit.getRefB());
                   preparedStatement.setDate(5, java.sql.Date.valueOf(visit.getDateVisit()));
                   int rowsAffected = preparedStatement.executeUpdate();
                   clear();

                   if (rowsAffected > 0) {
                       Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                       successAlert.setTitle("Succès");
                       successAlert.setHeaderText(null);
                       successAlert.setContentText("La demande a été ajoutée avec succès.");
                       successAlert.showAndWait();
                   }
               }
           }
       } catch (NumberFormatException e) {
           Alert alert = new Alert(Alert.AlertType.ERROR);
           alert.setTitle("Erreur");
           alert.setHeaderText(null);
           alert.setContentText("Veuillez entrer des valeurs numériques valides.");
           alert.showAndWait();
       } catch (SQLException e) {
           System.out.println("SQL Error: " + e.getMessage());
       }

   }

    private int getNomMaison(String nom) throws SQLException {
       int ref_b = -1;
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tunvista", "root", "");
                         PreparedStatement preparedStatement = connection.prepareStatement("SELECT ref_b FROM maison WHERE nom = ?")) {
                        preparedStatement.setString(1, nom);
                        try (ResultSet resultSet = preparedStatement.executeQuery()) {
                            if (resultSet.next()) {
                                ref_b = resultSet.getInt("ref_b");
                            }
                        }
                    }
                    return ref_b;
                }
    private void populateComboBox() {
        ObservableList<String> nomMaisonList = FXCollections.observableArrayList();

        try {
            String url = "jdbc:mysql://localhost:3306/tunvista";
            String username = "root";
            String password = "";
            Connection connection = DriverManager.getConnection(url, username, password);
            String sql = "SELECT ref_b, nom FROM maison";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String nomRestaurant = resultSet.getString("nom");
                nomMaisonList.add(nomRestaurant);
            }
            comboV.setItems(nomMaisonList);
            resultSet.close();

        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }
    void clear (){
        nomV.setText(null);
        numV.setText(null);
        emailV.setText(null);
        comboV.setValue(null);
        dateV.setValue(null);
    }
    @FXML
    void initialize() {

       populateComboBox();
    }

}