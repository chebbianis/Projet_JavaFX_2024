package com.example.pi.Controllers;

import com.example.pi.DB.DBUtils;
import com.example.pi.Services.SendSmsService;
import com.github.sarxos.webcam.Webcam;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.bytedeco.opencv.opencv_videoio.CvCapture;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.security.SecureRandom;
import java.sql.*;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class SignUpController implements Initializable {
    @FXML
    private Button button_showPassword;
    @FXML
    private Button button_generate;
    @FXML
    private Button button_Signup;

    @FXML
    private Button button_login;

    @FXML
    private TextField tf_email;

    @FXML
    private PasswordField pf_password;

    @FXML
    private ComboBox<String> cb_region;

    @FXML
    private TextField tf_adress;

    @FXML
    private TextField tf_city;

    @FXML
    private TextField tf_firstname;

    @FXML
    private TextField tf_lastname;
    @FXML
    private TextField tf_visiblePassword;

    private boolean isPasswordVisible = false;

    @FXML
    private Button verifyButton;

    @FXML
    private ImageView webcamView;

    @FXML
    private void togglePasswordVisibility(ActionEvent event) {
        removeCharCountListener();
        if (isPasswordVisible) {
            // Si le mot de passe est visible, basculer vers le PasswordField
            pf_password.setText(tf_visiblePassword.getText());
            pf_password.setVisible(true);
            tf_visiblePassword.setVisible(false);
        } else {
            // Sinon, basculer vers le TextField
            tf_visiblePassword.setText(pf_password.getText());
            tf_visiblePassword.setVisible(true);
            pf_password.setVisible(false);
        }

        // Inverser l'état de la visibilité du mot de passe
        isPasswordVisible = !isPasswordVisible;
    }

    private void removeCharCountListener() {
        // Récupérer le parent du PasswordField ou du TextField
        Parent parentContainer;
        if (pf_password.isVisible()) {
            parentContainer = pf_password.getParent();
        } else {
            parentContainer = tf_visiblePassword.getParent();
        }

        // Vérifier le type du parent et le supprimer si c'est un VBox
        if (parentContainer instanceof VBox) {
            VBox vboxParent = (VBox) parentContainer;
            vboxParent.getChildren().removeIf(node -> node instanceof HBox);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        addCharCountListenerEmail(tf_email);
        fillRegionComboBox();
        addCharCountListener(tf_firstname);
        addCharCountListener(tf_lastname);
        addCharCountListener(tf_city);
        addCharCountListener(tf_adress);





        button_generate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Générer un mot de passe aléatoire de 10 caractères
                String generatedPassword = generatePassword(15);

                // Vérifier si le mot de passe est actuellement visible
                if (isPasswordVisible) {
                    // Si le mot de passe est visible, afficher le mot de passe généré dans le TextField
                    tf_visiblePassword.setText(generatedPassword);
                    tf_visiblePassword.setVisible(true);
                    pf_password.setVisible(false);
                } else {
                    // Sinon, afficher le mot de passe généré dans le PasswordField
                    pf_password.setText(generatedPassword);
                    pf_password.setVisible(true);
                    tf_visiblePassword.setVisible(false);
                }
            }
        });


        button_Signup.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Vérifiez si tous les champs sont remplis et si la région est sélectionnée
                if (!tf_email.getText().trim().isEmpty() && !pf_password.getText().trim().isEmpty()
                        && !tf_firstname.getText().trim().isEmpty() && !tf_lastname.getText().trim().isEmpty()
                        && !tf_adress.getText().trim().isEmpty() && !tf_city.getText().trim().isEmpty()
                        && cb_region.getValue() != null) {
                    // Vérifiez si l'e-mail est valide
                    if (isValidEmail(tf_email.getText())) {
                        // Vérifiez la longueur des champs first name, last name, adress et city
                        if (isValidLength(tf_firstname.getText()) && isValidLength(tf_lastname.getText())
                                && isValidLength(tf_adress.getText()) && isValidLength(tf_city.getText())) {

                            // Demander le numéro de téléphone
                            TextInputDialog phoneDialog = new TextInputDialog();
                            phoneDialog.setTitle("Phone Number");
                            phoneDialog.setHeaderText(null);
                            phoneDialog.setContentText("Please enter your phone number:");

                            // Afficher la boîte de dialogue et attendre la réponse de l'utilisateur
                            String phoneNumber = phoneDialog.showAndWait().orElse(null);

                            // Vérifier si l'utilisateur a annulé l'opération
                            if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
                                // L'utilisateur a annulé l'opération, ne faites rien
                                return;
                            }

                            // Vérifier si le numéro de téléphone est valide (vous pouvez implémenter votre propre logique ici)
                            if (!phoneNumber.matches("\\d{8}")) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setContentText("Invalid phone number format");
                                alert.show();
                                return;
                            }

                            // Générer un code de confirmation aléatoire
                            Random random = new Random();
                            int confirmationCode = random.nextInt(9000) + 1000; // Génère un nombre aléatoire entre 1000 et 9999

                            System.out.println("le code de confirmation est : "+confirmationCode);








                            //SendSmsService.sendSMS(phoneNumber,"salut, le code de confirmation est : "+confirmationCode);








                            // Afficher une boîte de dialogue pour demander le code de confirmation
                            TextInputDialog confirmationDialog = new TextInputDialog();
                            confirmationDialog.setTitle("Confirmation Code");
                            confirmationDialog.setHeaderText(null);
                            confirmationDialog.setContentText("Please enter the confirmation code sent to your phone:");

                            // Afficher la boîte de dialogue et attendre la réponse de l'utilisateur
                            String userInput = confirmationDialog.showAndWait().orElse(null);

                            // Vérifier si l'utilisateur a annulé ou si le champ est vide
                            if (userInput == null || userInput.trim().isEmpty()) {
                                // L'utilisateur a annulé l'opération, ne faites rien
                                return;
                            }

                            // Vérifier si le code saisi correspond au code généré
                            if (userInput.trim().equals(String.valueOf(confirmationCode))) {
                                // Le code de confirmation est correct, continuer le processus d'inscription
                                // Récupérez l'identifiant de région à partir du nom de région sélectionné
                                int regionId = getRegionIdByName(cb_region.getValue());
                                // Enregistrez l'utilisateur avec tous les champs
                                DBUtils.signUpUser(event, tf_email.getText(), pf_password.getText(),
                                        tf_firstname.getText(), tf_lastname.getText(),
                                        tf_adress.getText(), tf_city.getText(), regionId);
                            } else {
                                // Le code de confirmation est incorrect, afficher un message d'erreur
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setContentText("Incorrect confirmation code. Please try again.");
                                alert.show();
                            }
                        } else {
                            // Affichez un message d'erreur si la longueur des champs est invalide
                            System.out.println("Invalid length for some fields");
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setContentText("Invalid length for some fields! Fields must be between 3 and 50 characters.");
                            alert.show();
                        }
                    } else {
                        // Si l'e-mail n'est pas valide, affichez un message d'erreur
                        System.out.println("Invalid email format");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Invalid email format");
                        alert.show();
                    }
                } else {
                    // Si l'un des champs est vide, affichez un message d'erreur
                    System.out.println("Please fill in all information");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please fill in all information to sign up!");
                    alert.show();
                }
            }
        });

        button_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    // Chargez le fichier FXML de la page de connexion
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/pi/hello-view.fxml"));
                    Parent root = fxmlLoader.load();

                    // Créez une nouvelle scène
                    Scene scene = new Scene(root);

                    // Obtenez la référence de la scène actuelle
                    Stage stage = (Stage) button_login.getScene().getWindow();

                    // Définissez la nouvelle scène
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean isValidLength(String text) {
        int length = text.length();
        return length > 2 && length <= 50;
    }

    private void fillRegionComboBox() {
        ObservableList<String> regionNames = FXCollections.observableArrayList();

        // Connexion à la base de données pour récupérer les noms de région
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement("SELECT nom FROM region");
             ResultSet resultSet = statement.executeQuery()) {

            // Ajouter les noms de région à la liste observable
            while (resultSet.next()) {
                regionNames.add(resultSet.getString("nom"));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Gérer les erreurs de connexion ou de requête SQL
        }

        // Ajouter les noms de région à la ComboBox
        cb_region.setItems(regionNames);
    }

    private int getRegionIdByName(String regionName) {
        int regionId = 0;
        String query = "SELECT id FROM region WHERE nom = ?";
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, regionName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    regionId = resultSet.getInt("id");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de l'ID de la région", e);
        }
        return regionId;
    }


    public static String generatePassword(int length) {
        StringBuilder password = new StringBuilder();
        Random random = new SecureRandom();

        // Définir les ensembles de caractères pour chaque catégorie
        String lowercaseChars = "abcdefghijklmnopqrstuvwxyz";
        String uppercaseChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String numericChars = "0123456789";
        String specialChars = "!@#$%^&*()-_=+";

        // Assurez-vous d'avoir au moins un caractère de chaque catégorie dans le mot de passe
        password.append(lowercaseChars.charAt(random.nextInt(lowercaseChars.length())));
        password.append(uppercaseChars.charAt(random.nextInt(uppercaseChars.length())));
        password.append(numericChars.charAt(random.nextInt(numericChars.length())));
        password.append(specialChars.charAt(random.nextInt(specialChars.length())));

        // Ajouter les caractères restants du mot de passe
        for (int i = 4; i < length; i++) {
            // Choisissez un caractère aléatoire parmi tous les caractères possibles
            String allChars = lowercaseChars + uppercaseChars + numericChars + specialChars;
            char randomChar = allChars.charAt(random.nextInt(allChars.length()));
            // Ajouter le caractère aléatoire au mot de passe
            password.append(randomChar);
        }

        // Mélanger les caractères du mot de passe pour plus de sécurité
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(length);
            char temp = password.charAt(i);
            password.setCharAt(i, password.charAt(randomIndex));
            password.setCharAt(randomIndex, temp);
        }

        return password.toString();
    }


    public String getRegionNameById(int regionId) {
        String regionName = "";
        Connection conn = getConnection();
        String query = "SELECT nom FROM region WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, regionId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    regionName = resultSet.getString("nom");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return regionName;
    }

    public Connection getConnection(){
        Connection connection;
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/tunvista_integration",
                    "root",
                    "mohamedomar"
            );
            return connection;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void addCharCountListener(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                // Si le champ est vide, réinitialiser le style
                textField.setStyle("");
            } else {
                // Vérifier la longueur du texte
                int length = newValue.length();
                if (length <= 2 || length > 50) {
                    // Si la longueur est inférieure à 2 ou supérieure à 50, définir le style pour afficher en rouge
                    textField.setStyle("-fx-border-color: red;");
                } else {
                    // Sinon, réinitialiser le style
                    textField.setStyle("");
                }
            }
        });
    }


    private void addCharCountListenerEmail(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                // Si le champ est vide, réinitialiser le style
                textField.setStyle("");
            } else {
                // Vérifiez si l'e-mail est valide
                if (isValidEmail(newValue)) {
                    // Si l'e-mail est valide, réinitialiser le style
                    textField.setStyle("");
                } else {
                    // Si l'e-mail n'est pas valide, définissez le style pour afficher en rouge
                    textField.setStyle("-fx-border-color: red;");
                }
            }
        });
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
