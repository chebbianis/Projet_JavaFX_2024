package com.example.pi.DB;

import com.example.pi.Entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;


public class DBUtils {

    private static ActionEvent event;
    private static String fxmlFile;
    private static String title;
    private static String username;

    public static void changeScene(ActionEvent event, String fxmlPath, String title, String username) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(DBUtils.class.getResource(fxmlPath));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Affichez des informations de débogage supplémentaires ici
        }
    }

//    public static void changeScene(ActionEvent event, String fxmlFile, String title, String username){
//        Parent root = null;
//
//        if (username != null){
//            try {
//                FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
//                root = loader.load();
//                LoggedInController loggedInController = loader.getController();
//                loggedInController.setUserInformation(username);
//            } catch (IOException e) {
//                e.printStackTrace();
//                System.out.println("Error loading FXML file: " + e.getMessage());
//            }
//        } else {
//            try {
//                // Utilisez la variable fxmlFile pour charger le fichier FXML
//                if (DBUtils.fxmlFile != null) {
//                    root = FXMLLoader.load(Objects.requireNonNull(DBUtils.class.getResource(fxmlFile)));
//                } else {
//                    System.err.println("FXML file not found: " + fxmlFile);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        stage.setTitle(title);
//        stage.setScene(new Scene(root, 600, 400));
//        stage.show();
//    }

    public static void signUpUser(ActionEvent event, String username, String password) {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/javafx",
                    "root",
                    "mohamedomar"
            );
            psCheckUserExists = connection.prepareStatement("SELECT * FROM user WHERE username = ? ");
            psCheckUserExists.setString(1, username);
            resultSet = psCheckUserExists.executeQuery();

            if (resultSet.isBeforeFirst()) {
                System.out.println("USER already exists !");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You cannot use this username!");
                alert.show();
            } else {
                // Créer un nouvel objet User avec les informations fournies
                User newUser = new User(username, password);

                // Utiliser PreparedStatement pour insérer le nouvel utilisateur dans la base de données
                psInsert = connection.prepareStatement("INSERT INTO user (username, password) VALUES (?, ?)");
                psInsert.setString(1, newUser.getUsername());
                psInsert.setString(2, newUser.getPassword());
                psInsert.executeUpdate();

                System.out.println("Sign up done !!!");
                changeScene(event, "/com/example/pi/logged-in.fxml", "Welcome!", newUser.getUsername());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            // Gestion des ressources
        }
    }


//    public static void signUpUser(ActionEvent event,String username,String password){
//        Connection connection = null;
//        PreparedStatement psInsert = null;
//        PreparedStatement psCheckUserExists = null;
//        ResultSet resultSet = null;
//
//        try {
//            connection = DriverManager.getConnection(
//                    "jdbc:mysql://localhost:3306/javafx",
//                    "root",
//                    "mohamedomar"
//            );
//            psCheckUserExists = connection.prepareStatement("SELECT * FROM user WHERE username = ? ");
//            psCheckUserExists.setString(1,username);
//            resultSet = psCheckUserExists.executeQuery();
//
//            if (resultSet.isBeforeFirst()){
//                System.out.println("USER already exists !");
//                Alert alert = new Alert(Alert.AlertType.ERROR);
//                alert.setContentText(" you connot use this username !");
//                alert.show();
//            }else {
//                psInsert = connection.prepareStatement("INSERT INTO user (username,password) VALUES (?,?)");
//                psInsert.setString(1,username);
//                psInsert.setString(2,password);
//                psInsert.executeUpdate();
//
//                System.out.println("sign up done !!!");
//                changeScene(event,"/com/example/pi/logged-in.fxml","Welcome !",username);
//
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        } finally {
//            if (resultSet != null){
//                try {
//                    resultSet.close();
//                } catch (SQLException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            if (psCheckUserExists != null){
//                try {
//                    assert psInsert != null;
//                    psInsert.close();
//                } catch (SQLException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            if (connection !=null ){
//                try {
//                    connection.close();
//                } catch (SQLException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }
//    }

    public static void loginUser (ActionEvent event, String username, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/javafx",
                    "root",
                    "mohamedomar"
            );
            preparedStatement = connection.prepareStatement("SELECT password FROM user WHERE username = ? ");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                System.out.println("USER not found !");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(" provided credentials are incorrect !");
                alert.show();
            } else {
                resultSet.next(); // Move cursor to the first row
                String retrievedPassword = resultSet.getString("password");
                if (retrievedPassword.equals(password)) {
                    changeScene(event, "/com/example/pi/logged-in.fxml", "Welcome !", username);
                } else {
                    System.out.println("password did not match !!");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("the provided credentials are incorrect !!");
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
    }
