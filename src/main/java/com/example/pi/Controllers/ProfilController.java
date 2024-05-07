package com.example.pi.Controllers;

import com.example.pi.DB.DBUtils;
import com.example.pi.Entities.User;
import com.example.pi.Services.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.mindrot.jbcrypt.BCrypt;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import static java.sql.DriverManager.getConnection;

public class ProfilController implements Initializable {
    @FXML
    private ComboBox<String> cb_region;

    @FXML
    private Text text_welcome;

    @FXML
    private TextField tf_adresse;

    @FXML
    private TextField tf_email;

    @FXML
    private TextField tf_firstname;

    @FXML
    private TextField tf_lastname;

    @FXML
    private PasswordField tf_password;

    @FXML
    private PasswordField tf_newPassword;

    @FXML
    private PasswordField tf_confirmPassword;

    @FXML
    private TextField tf_ville;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String welcomeMessage = "Bienvenue, " + DBUtils.getFullNameFromEmail(UserSession.getInstance().getEmail());
        text_welcome.setText(welcomeMessage);

        fillProfileFields();
        fillRegionComboBox();
    }

    private void fillProfileFields() {
        User user = DBUtils.getUserByEmail(UserSession.getInstance().getEmail());
        if (user != null) {
            tf_email.setText(user.getEmail());
            tf_firstname.setText(user.getFirstName());
            tf_lastname.setText(user.getLastName());
            tf_adresse.setText(user.getAdresse());
            tf_ville.setText(user.getVille());
        }
    }

    private void fillRegionComboBox() {
        ObservableList<String> regionNames = FXCollections.observableArrayList();

        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement("SELECT nom FROM region");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                regionNames.add(resultSet.getString("nom"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        cb_region.setItems(regionNames);
    }

    @FXML
    private void updateProfile() {
        User user = DBUtils.getUserByEmail(UserSession.getInstance().getEmail());

        if (user != null) {
            String query = "UPDATE user SET ";
            boolean needComma = false;

            if (tf_firstname.getText() != null && !tf_firstname.getText().isEmpty() && !tf_firstname.getText().equals(user.getFirstName())) {
                query += (needComma ? ", " : "") + "first_name = ?";
                needComma = true;
            }

            if (tf_lastname.getText() != null && !tf_lastname.getText().isEmpty() && !tf_lastname.getText().equals(user.getLastName())) {
                query += (needComma ? ", " : "") + "last_name = ?";
                needComma = true;
            }

            if (tf_email.getText() != null && !tf_email.getText().isEmpty() && !tf_email.getText().equals(user.getEmail())) {
                query += (needComma ? ", " : "") + "email = ?";
                needComma = true;
            }

            if (tf_password.getText() != null && !tf_password.getText().isEmpty()) {
                query += (needComma ? ", " : "") + "password = ?";
                needComma = true;
            }

            query += " WHERE email = ?";

            if (needComma) {
                try (Connection conn = getConnection();
                     PreparedStatement statement = conn.prepareStatement(query)) {
                    int parameterIndex = 1;

                    if (tf_firstname.getText() != null && !tf_firstname.getText().isEmpty() && !tf_firstname.getText().equals(user.getFirstName())) {
                        statement.setString(parameterIndex++, tf_firstname.getText());
                    }

                    if (tf_lastname.getText() != null && !tf_lastname.getText().isEmpty() && !tf_lastname.getText().equals(user.getLastName())) {
                        statement.setString(parameterIndex++, tf_lastname.getText());
                    }

                    if (tf_email.getText() != null && !tf_email.getText().isEmpty() && !tf_email.getText().equals(user.getEmail())) {
                        statement.setString(parameterIndex++, tf_email.getText());
                    }

                    if (tf_password.getText() != null && !tf_password.getText().isEmpty()) {
                        statement.setString(parameterIndex++, tf_password.getText());
                    }

                    statement.setString(parameterIndex, user.getEmail());

                    int rowsUpdated = statement.executeUpdate();
                    if (rowsUpdated > 0) {
                        showAlert(Alert.AlertType.INFORMATION, "Mise à jour réussie", "Votre profil a été mis à jour avec succès !");
                        System.out.println("Profil utilisateur mis à jour avec succès !");
                    } else {
                        showAlert(Alert.AlertType.WARNING, "Aucune modification", "Aucune modification détectée.");
                        System.out.println("Aucune modification détectée.");
                    }
                } catch (SQLException e) {
                    throw new RuntimeException("Erreur lors de la mise à jour du profil utilisateur", e);
                }
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Aucune modification", "Aucune modification détectée.");
                System.out.println("Aucune modification détectée.");
            }
        } else {
            System.out.println("Utilisateur non trouvé.");
        }
    }



    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



    private Connection getConnection(){
        Connection connection;
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/tunvista",
                    "root",
                    "mohamedomar"
            );
            return connection;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
