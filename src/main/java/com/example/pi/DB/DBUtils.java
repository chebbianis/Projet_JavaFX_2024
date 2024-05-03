package com.example.pi.DB;

import com.example.pi.Controllers.EmailController;
import com.example.pi.Entities.User;
import com.example.pi.Services.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;



public class DBUtils {

    private static ActionEvent event;
    private static String fxmlFile;
    private static String title;
    private static String username;
    private static Object sceneData;


    public static void changeScene(ActionEvent event, String fxmlPath, String title, Object data) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(DBUtils.class.getResource(fxmlPath));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();
            sceneData = data;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void changeSceneWithObject(ActionEvent event, String fxmlPath, String title, Object data) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(DBUtils.class.getResource(fxmlPath));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle(title);
            stage.setScene(scene);

            // Pass data to the controller of the new scene if needed
            if (fxmlLoader.getController() instanceof EmailController) {
                EmailController controller = fxmlLoader.getController();
                controller.setData(data);
            }

            stage.show();
            sceneData = data;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Object getSceneData() {
        return sceneData;
    }

    public static void signUpUser(ActionEvent event, String email, String password, String firstName, String lastName, String address, String city, int regionId) {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/tunvista_integration",
                    "root",
                    "mohamedomar"
            );
            psCheckUserExists = connection.prepareStatement("SELECT * FROM user WHERE email = ?");
            psCheckUserExists.setString(1, email);
            resultSet = psCheckUserExists.executeQuery();

            if (resultSet.isBeforeFirst()) {
                System.out.println("User already exists!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You cannot use this email!");
                alert.show();
            } else {
                String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

                // Utiliser PreparedStatement pour insérer le nouvel utilisateur dans la base de données
                psInsert = connection.prepareStatement("INSERT INTO user (email, password, first_name, last_name, adresse, ville, region_id, roles) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                psInsert.setString(1, email);
                psInsert.setString(2, hashedPassword);
                psInsert.setString(3, firstName);
                psInsert.setString(4, lastName);
                psInsert.setString(5, address);
                psInsert.setString(6, city);
                psInsert.setInt(7, regionId);
                psInsert.setString(8, "[\"ROLE_USER\"]");
                psInsert.executeUpdate();

                System.out.println("Sign up done!");
                UserSession.getInstance().setEmail(email);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Sign up done!");
                alert.show();
                changeScene(event, "/com/example/pi/logged-in.fxml", "Welcome!", email);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            // Fermer les ressources JDBC
            try {
                if (resultSet != null) resultSet.close();
                if (psCheckUserExists != null) psCheckUserExists.close();
                if (psInsert != null) psInsert.close();
                if (connection != null) connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void loginUser(ActionEvent event, String email, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/tunvista_integration",
                    "root",
                    "mohamedomar"
            );
            preparedStatement = connection.prepareStatement("SELECT password FROM user WHERE email = ? ");
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                System.out.println("USER not found !");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(" USER not found !");
                alert.show();
            } else {

                resultSet.next(); // Move cursor to the first row
                String retrievedPassword = resultSet.getString("password");
                String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());


                if (BCrypt.checkpw(password, retrievedPassword)) {
                    UserSession.getInstance().setEmail(email);
                    changeScene(event, "/com/example/pi/logged-in.fxml", "Welcome !", email);
                    System.out.println("authentication done !!");
                } else {
                    System.out.println("password did not match !!");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("password did not match !!");
                    alert.show();
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            if (resultSet != null){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (preparedStatement != null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (connection !=null ){
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static String getFullNameFromEmail(String email) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String fullName = ""; // Chaîne de caractères pour stocker le prénom et le nom de l'utilisateur

        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/tunvista_integration",
                    "root",
                    "mohamedomar"
            );
            preparedStatement = connection.prepareStatement("SELECT first_name, last_name FROM user WHERE email = ?");
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Récupérer le prénom et le nom de l'utilisateur à partir du résultat de la requête
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                // Concaténer le prénom et le nom dans une seule chaîne de caractères
                fullName = firstName + " " + lastName;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            // Fermer les ressources JDBC
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return fullName;
    }
 }
