package com.example.pi.Controllers;

import com.example.pi.DB.DBUtils;
import com.example.pi.Entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class UserController implements Initializable {
    @FXML
    private Button button_logout;
    @FXML
    private Button btn_insert;
    @FXML
    private Button btn_update;
    @FXML
    private Button btn_delete;
    @FXML
    private TextField tf_id;
    @FXML
    private TextField tf_password;
    @FXML
    private TextField tf_username;
    @FXML
    private TextField tf_email;
    @FXML
    private TextField tf_firstname;
    @FXML
    private TextField tf_lastname;
    @FXML
    private TextField tf_location;
    @FXML
    private ComboBox<String> cb_role;
    @FXML
    private TableView<User> table_user;
    @FXML
    private TableColumn<User, Integer> col_id;
    @FXML
    private TableColumn<User, String> col_username;
    @FXML
    private TableColumn<User, String> col_email;
    @FXML
    private TableColumn<User, String> col_firstname;
    @FXML
    private TableColumn<User, String> col_lastname;
    @FXML
    private TableColumn<User, String> col_location;
    @FXML
    private TableColumn<User, String> col_role;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        button_logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event,"/com/example/pi/hello-view.fxml","Login",null);
            }
        });

        btn_insert.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                insertUser();
                showUsers();
            }
        });

        btn_update.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                updateUser();
                showUsers();
            }
        });

        btn_delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                deleteUser();
                showUsers();

            }
        });
        table_user.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Remplir les champs du formulaire avec les données de l'utilisateur sélectionné
                tf_username.setText(newSelection.getUsername());
                tf_password.setText(newSelection.getPassword());
                tf_firstname.setText(newSelection.getFirstName());
                tf_lastname.setText(newSelection.getLastName());
                tf_email.setText(newSelection.getEmail());
                tf_location.setText(newSelection.getLocation());
                cb_role.setValue(newSelection.getRoleUser());
            } else {
                // Si aucune sélection n'est faite, effacer les champs du formulaire
                clearForm();
            }
        });
        showUsers();
    }
    private void clearForm() {
        tf_username.clear();
        tf_password.clear();
        tf_firstname.clear();
        tf_lastname.clear();
        tf_email.clear();
        tf_location.clear();
        cb_role.getSelectionModel().clearSelection();
    }


    public Connection getConnection(){
    Connection connection;
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/javafx",
                    "root",
                    "mohamedomar"
            );
            return connection;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ObservableList<User> getUserList(){
        ObservableList userList = FXCollections.observableArrayList();
        Connection conn = getConnection();
        String query = "SELECT * FROM user";
        Statement statement;
        ResultSet resultSet;
        try {
            statement = conn.createStatement();
            resultSet = statement.executeQuery(query);
            User user;
            while ((resultSet.next())){
                user = new User(
                        resultSet.getInt("user_id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("email"),
                        resultSet.getString("location"),
                        resultSet.getString("role")
                        );
                userList.add(user);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userList;
    }

    public void showUsers(){
        ObservableList<User> list = getUserList();

        col_id.setCellValueFactory(new PropertyValueFactory<User,Integer>("id"));
        col_username.setCellValueFactory(new PropertyValueFactory<User,String>("username"));
        col_firstname.setCellValueFactory(new PropertyValueFactory<User,String>("firstName"));
        col_lastname.setCellValueFactory(new PropertyValueFactory<User,String>("lastName"));
        col_email.setCellValueFactory(new PropertyValueFactory<User,String>("email"));
        col_location.setCellValueFactory(new PropertyValueFactory<User,String>("location"));
        col_role.setCellValueFactory(new PropertyValueFactory<User,String>("roleUser"));

        table_user.setItems(list);
    }

    private void insertUser() {
        // Vérifier si le nom d'utilisateur existe déjà
        if (isUsernameExists(tf_username.getText())) {
            // Afficher un message d'erreur
            System.out.println("Erreur : Le nom d'utilisateur existe déjà.");
            return; // Sortir de la méthode sans insérer l'utilisateur
        }

        // Le nom d'utilisateur n'existe pas encore, procéder à l'insertion
        String query = "INSERT INTO user (username, password, first_name, last_name, email, location, role) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, tf_username.getText());
            statement.setString(2, tf_password.getText());
            statement.setString(3, tf_firstname.getText());
            statement.setString(4, tf_lastname.getText());
            statement.setString(5, tf_email.getText());
            statement.setString(6, tf_location.getText());
            statement.setString(7, cb_role.getValue());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Utilisateur inséré avec succès !");
                // Actualiser la table des utilisateurs après l'insertion
                showUsers();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'insertion de l'utilisateur", e);
        }
    }

    private boolean isUsernameExists(String username) {
        String query = "SELECT COUNT(*) FROM user WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la vérification du nom d'utilisateur", e);
        }
        return false;
    }

    private void updateUser() {
        User selectedUser = table_user.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            String query = "UPDATE user SET ";
            boolean needComma = false;

            if (tf_username.getText() != null &&!tf_username.getText().isEmpty() && !tf_username.getText().equals(selectedUser.getUsername())) {
                query += "username = ?";
                needComma = true;
            }
            if (tf_password.getText() != null &&!tf_password.getText().isEmpty() && !tf_password.getText().equals(selectedUser.getPassword())) {
                query += (needComma ? ", " : "") + "password = ?";
                needComma = true;
            }
            if (tf_firstname.getText() != null &&!tf_firstname.getText().isEmpty() && !tf_firstname.getText().equals(selectedUser.getFirstName())) {
                query += (needComma ? ", " : "") + "first_name = ?";
                needComma = true;
            }
            if (tf_lastname.getText() != null && !tf_lastname.getText().isEmpty() && !tf_lastname.getText().equals(selectedUser.getLastName())) {
                query += (needComma ? ", " : "") + "last_name = ?";
                needComma = true;
            }
            if (tf_email.getText() != null &&!tf_email.getText().isEmpty() && !tf_email.getText().equals(selectedUser.getEmail())) {
                query += (needComma ? ", " : "") + "email = ?";
                needComma = true;
            }
            if (tf_location.getText() != null &&!tf_location.getText().isEmpty() && !tf_location.getText().equals(selectedUser.getLocation())) {
                query += (needComma ? ", " : "") + "location = ?";
                needComma = true;
            }
            if (cb_role.getValue() != null && !cb_role.getValue().isEmpty() && !cb_role.getValue().equals(selectedUser.getRoleUser())) {
                query += (needComma ? ", " : "") + "role = ?";
                needComma = true;
            }

            query += " WHERE user_id = ?";

            if (needComma) { // Si au moins un champ a été modifié
                try (Connection conn = getConnection();
                     PreparedStatement statement = conn.prepareStatement(query)) {
                    int parameterIndex = 1;
                    if (!tf_username.getText().isEmpty() && !tf_username.getText().equals(selectedUser.getUsername())) {
                        statement.setString(parameterIndex++, tf_username.getText());
                    }
                    if (!tf_password.getText().isEmpty() && !tf_password.getText().equals(selectedUser.getPassword())) {
                        statement.setString(parameterIndex++, tf_password.getText());
                    }
                    if (!tf_firstname.getText().isEmpty() && !tf_firstname.getText().equals(selectedUser.getFirstName())) {
                        statement.setString(parameterIndex++, tf_firstname.getText());
                    }
                    if (!tf_lastname.getText().isEmpty() && !tf_lastname.getText().equals(selectedUser.getLastName())) {
                        statement.setString(parameterIndex++, tf_lastname.getText());
                    }
                    if (!tf_email.getText().isEmpty() && !tf_email.getText().equals(selectedUser.getEmail())) {
                        statement.setString(parameterIndex++, tf_email.getText());
                    }
                    if (!tf_location.getText().isEmpty() && !tf_location.getText().equals(selectedUser.getLocation())) {
                        statement.setString(parameterIndex++, tf_location.getText());
                    }
                    if (cb_role.getValue() != null && !cb_role.getValue().isEmpty() && !cb_role.getValue().equals(selectedUser.getRoleUser())) {
                        statement.setString(parameterIndex++, cb_role.getValue());
                    }
                    statement.setInt(parameterIndex, selectedUser.getId());

                    int rowsUpdated = statement.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Utilisateur mis à jour avec succès !");
                        showUsers(); // Actualiser la table des utilisateurs après la mise à jour
                    }
                } catch (SQLException e) {
                    throw new RuntimeException("Erreur lors de la mise à jour de l'utilisateur", e);
                }
            } else {
                System.out.println("Aucune modification détectée.");
            }
        } else {
            System.out.println("Veuillez sélectionner un utilisateur à mettre à jour.");
        }
    }

    private void deleteUser() {
        User selectedUser = table_user.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            String query = "DELETE FROM user WHERE user_id = ?";

            try (Connection conn = getConnection();
                 PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setInt(1, selectedUser.getId());

                int rowsDeleted = statement.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("Utilisateur supprimé avec succès !");
                    showUsers(); // Actualiser la table des utilisateurs après la suppression
                }
            } catch (SQLException e) {
                throw new RuntimeException("Erreur lors de la suppression de l'utilisateur", e);
            }
        } else {
            System.out.println("Veuillez sélectionner un utilisateur à supprimer.");
        }
    }

}
