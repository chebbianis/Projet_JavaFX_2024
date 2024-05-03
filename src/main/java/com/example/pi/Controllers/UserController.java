package com.example.pi.Controllers;

import com.example.pi.DB.DBUtils;
import com.example.pi.Entities.Region;
import com.example.pi.Entities.User;
import com.example.pi.Services.GeminiAPI;
import com.example.pi.Services.UserSession;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserController implements Initializable {

    @FXML
    private Button btn_send;
    @FXML
    private TableColumn<User, String> col_regionId;
    @FXML
    private TableColumn<User, String> col_ville;
    @FXML
    private TableColumn<User, String> col_adresse;
    @FXML
    private Button button_logout;
    @FXML
    private CheckBox ch_admin;
    @FXML
    private CheckBox ch_user;
    @FXML
    private Button btn_insert;
    @FXML
    private Button btn_update;
    @FXML
    private Button btn_delete;
    @FXML
    private Button btn_clear;
    @FXML
    private TextField tf_password;
    @FXML
    private TextField tf_email;
    @FXML
    private TextField tf_firstname;
    @FXML
    private TextField tf_lastname;
    @FXML
    private TextField tf_ville;
    @FXML
    private ComboBox<String> cb_role;
    @FXML
    private TableView<User> table_user;
    @FXML
    private TableColumn<User, Integer> col_id;
    @FXML
    private ComboBox<String> cb_region;
    @FXML
    private TableColumn<User, String> col_email;
    @FXML
    private TableColumn<User, String> col_firstname;
    @FXML
    private TableColumn<User, String> col_lastname;
    @FXML
    private TableColumn<User, String> col_role;
    @FXML
    private TableColumn<User, String> col_region;
    @FXML
    private TextField tf_adresse;
    @FXML
    private TextField tf_search;
    @FXML
    private Button btn_sms;
    @FXML
    private Button btn_malek;

    private final Image iconValid = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/pi/icons/check.png")));
    private final Image iconInvalid = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/pi/icons/cross.png")));



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addCharCountListenerEmail(tf_email,-3);
        addCharCountListener(tf_firstname,56);
        addCharCountListener(tf_lastname,10);
        addCharCountListener(tf_adresse,10);
        addCharCountListener(tf_ville,10);
        addCharCountListenerPassword(tf_password,-220);

        String userEmail = UserSession.getInstance().getEmail();

        System.out.println("email utilisateur est : "+userEmail);
        System.out.println("System.getenv(\"GPTKey\") = "+ System.getenv("GPTKey"));
        System.out.println("System.getenv(\"twilioSid\") = "+System.getenv("twilioSid"));

//        addCharCountListenerEmail(tf_email,-175);

        fillRegionComboBox();
        addSearchListener();




        btn_send.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String email = tf_email.getText();

                // Vérifie si tf_email est vide, sinon utilise la valeur saisie
                if (tf_email.getText().isEmpty()) {
                    email = "";
                }
//                System.out.println("adresse mail dans userController est : "+email);

                DBUtils.changeSceneWithObject(event, "/com/example/pi/email.fxml", "Email", email);
            }
        });





        button_logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                UserSession.getInstance().clear();
                DBUtils.changeScene(event,"/com/example/pi/hello-view.fxml","Login",null);
            }
        });

        btn_sms.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event,"/com/example/pi/send-sms.fxml","Send SMS",null);
            }
        });

        btn_malek.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event,"/com/example/pi/AjouterMaison.fxml","test",null);
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
        btn_clear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                clearForm();
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
                tf_ville.setText(newSelection.getVille());
                tf_firstname.setText(newSelection.getFirstName());
                tf_lastname.setText(newSelection.getLastName());
                tf_email.setText(newSelection.getEmail());
                tf_adresse.setText(newSelection.getAdresse());
                cb_region.setValue(newSelection.getRegion().getName());

                // Traiter les rôles de l'utilisateur sélectionné
                String[] roles = newSelection.getRoles().split(", ");
//                for (String role : roles) {
//                    role = role.replaceAll("[\\[\"\\]]", "");
//                    System.out.println("role : "+role);
//                }
                ch_user.setSelected(false);
                ch_admin.setSelected(false);
                for (String role : roles) {
                    role = role.replaceAll("[\\[\"\\]]", "");
                    switch (role) {
                        case "ROLE_USER":
                            ch_user.setSelected(true);
                            break;
                        case "ROLE_ADMIN":
                            ch_admin.setSelected(true);
                            break;
                        // Ajouter d'autres cas selon vos besoins
                    }
                }
            } else {
                clearForm();
            }
        });

        showUsers();

//        try {
//            testGetRequest();
//            testPostRequest();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }


    private static void testGetRequest() throws IOException {
        System.out.println("Testing GET Request...");
        GeminiAPI geminiAPI = new GeminiAPI();
        String response = geminiAPI.getRequest("/exampleEndpoint");
        System.out.println("GET Response: " + response);
    }

    private static void testPostRequest() throws IOException {
        System.out.println("\nTesting POST Request...");
        GeminiAPI geminiAPI = new GeminiAPI();
        String requestBody = "{\"key\":\"value\"}";
        String postResponse = geminiAPI.postRequest("/exampleEndpoint", requestBody);
        System.out.println("POST Response: " + postResponse);
    }

    private void addSearchListener() {
        // Ajouter un écouteur de changement de texte au champ de recherche
        tf_search.textProperty().addListener((observable, oldValue, newValue) -> {
            // Appeler la méthode de recherche d'utilisateurs avec le nouveau texte saisi
            searchUsers(newValue);
        });
    }
    @FXML
    private void handleSearch(ActionEvent event) {
        String searchText = tf_search.getText();
        searchUsers(searchText);
    }


    private void searchUsers(String searchText) {
        ObservableList<User> resultList = FXCollections.observableArrayList();
        ObservableList<User> userList = getUserList();

        for (User user : userList) {
            // Vérifier si le nom et le prénom ne sont pas nuls avant d'effectuer la recherche
            if (user.getFirstName() != null && user.getLastName() != null) {
                // Recherche par nom ou prénom
                if (user.getFirstName().toLowerCase().contains(searchText.toLowerCase()) ||
                        user.getLastName().toLowerCase().contains(searchText.toLowerCase())) {
                    resultList.add(user);
                }
            }
        }

        // Mettre à jour l'affichage de la table des utilisateurs avec les résultats de la recherche
        table_user.setItems(resultList);
    }


    private void clearForm() {
        tf_password.clear();
        tf_firstname.clear();
        tf_lastname.clear();
        tf_email.clear();
        tf_adresse.clear(); // Ajout du champ d'adresse
        tf_ville.clear(); // Ajout du champ de ville
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


    private void addCharCountListenerEmail(TextField textField,int place) {
        Label validationLabel = new Label();
        ImageView validationIcon = new ImageView();

        // Définissez la taille de l'icône
        validationIcon.setFitWidth(16); // Largeur de l'icône
        validationIcon.setFitHeight(16); // Hauteur de l'icône

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                // Si la nouvelle valeur est nulle ou vide, effacez l'icône et le texte de validation
                validationIcon.setImage(null);
                validationLabel.setText("");
            } else {
                if (isValidEmail(newValue)) {
                    // Si l'e-mail est valide, afficher une icône de coche verte et un message vide
                    validationIcon.setImage(iconValid);
                    validationLabel.setText("");
                } else {
                    // Si l'e-mail est invalide, afficher une icône de croix rouge et un message d'erreur
                    validationIcon.setImage(iconInvalid);
                    validationLabel.setText("");
                }
            }
        });

        // Créez un conteneur HBox pour placer le label et l'icône horizontalement
        HBox container = new HBox(validationLabel, validationIcon);
        container.setSpacing(150); // Ajoutez un espace entre le label et l'icône

        // Ajoutez le conteneur HBox au conteneur parent du champ TextField (par exemple, un VBox)
        VBox parentContainer = (VBox) textField.getParent(); // Assurez-vous d'avoir le bon conteneur parent
        parentContainer.getChildren().add(container);

        // Réglez le positionnement du conteneur au-dessus du champ TextField
        VBox.setMargin(container, new Insets(place, 0, 0, 0)); // Ajustez les marges au besoin
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    private void addCharCountListener(TextField textField,int place) {
        Label charCountLabel = new Label();
        ImageView validationIcon = new ImageView();

        // Définissez la taille de l'icône
        validationIcon.setFitWidth(16); // Largeur de l'icône
        validationIcon.setFitHeight(16); // Hauteur de l'icône

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                // Si newValue est null, le champ de texte est vide, donc pas d'icône ni de texte de compteur de caractères
                validationIcon.setImage(null);
                charCountLabel.setText("");
            } else {
                int length = newValue.length();
                if (length == 0) {
                    validationIcon.setImage(null);
                    charCountLabel.setText("");
                } else if (length <= 2 || length > 50) {
                    // Afficher une icône de croix rouge pour indiquer une erreur de saisie
                    validationIcon.setImage(iconInvalid);
                    charCountLabel.setText(length + "/50");
                } else {
                    // Afficher une icône de coche verte pour indiquer une saisie valide
                    validationIcon.setImage(iconValid);
                    charCountLabel.setText(length + "/50");
                }
            }
            // Mettre à jour le texte du label avec le nombre de caractères saisis
        });

        // Créez un conteneur HBox pour placer le label et l'icône horizontalement
        HBox container = new HBox(charCountLabel, validationIcon);
        container.setSpacing(120); // Ajoutez un espace entre le label et l'icône

        // Ajoutez le conteneur HBox au conteneur parent du champ TextField (par exemple, un VBox)
        VBox parentContainer = (VBox) textField.getParent(); // Assurez-vous d'avoir le bon conteneur parent
        parentContainer.getChildren().add(container);

        // Réglez le positionnement du conteneur au-dessus du champ TextField
        VBox.setMargin(container, new Insets(place, 0, 0, 0)); // Ajustez les marges au besoin
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

    public ObservableList<User> getUserList(){
        ObservableList<User> userList = FXCollections.observableArrayList();
        Connection conn = getConnection();
        String query = "SELECT u.*, r.nom AS region_name FROM user u LEFT JOIN region r ON u.region_id = r.id";
        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()){
                Region region = new Region();
                int regionId = resultSet.getInt("region_id");
                String regionName = getRegionNameById(regionId);
                region.setName(regionName);
                User user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("adresse"),
                        resultSet.getString("ville"),
                        resultSet.getString("roles"),
                        region
                );
                userList.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userList;
    }

    public void showUsers(){
        String searchText = tf_search.getText();
        if (searchText.isEmpty()) {
                ObservableList<User> list = getUserList();
                col_id.setCellValueFactory(new PropertyValueFactory<User,Integer>("id"));
                col_role.setCellValueFactory(new PropertyValueFactory<User,String>("roles"));
                col_firstname.setCellValueFactory(new PropertyValueFactory<User,String>("firstName"));
                col_lastname.setCellValueFactory(new PropertyValueFactory<User,String>("lastName"));
                col_email.setCellValueFactory(new PropertyValueFactory<User,String>("email"));
                col_adresse.setCellValueFactory(new PropertyValueFactory<User,String>("adresse"));
                col_ville.setCellValueFactory(new PropertyValueFactory<User,String>("ville"));
                col_region.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRegion().getName()));

                table_user.setItems(list);}
        else {
            searchUsers(searchText);
        }

    }



    private boolean userExistsWithEmail(String email) {
        String query = "SELECT COUNT(*) FROM user WHERE email = ?";
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la vérification de l'existence de l'utilisateur avec l'e-mail spécifié", e);
        }
        return false;
    }

    private void insertUser() {
        // Vérifier si les champs sont vides
        if (tf_email.getText() == null || tf_email.getText().isEmpty() ||
                tf_password.getText() == null || tf_password.getText().isEmpty() ||
                tf_firstname.getText() == null || tf_firstname.getText().isEmpty() ||
                tf_lastname.getText() == null || tf_lastname.getText().isEmpty() ||
                tf_adresse.getText() == null || tf_adresse.getText().isEmpty() ||
                tf_ville.getText() == null || tf_ville.getText().isEmpty() ||
                cb_region.getValue() == null || cb_region.getValue().isEmpty() ||
                (!ch_user.isSelected() && !ch_admin.isSelected())) {
            // Afficher un message d'erreur si l'un des champs est null ou vide
            System.out.println("Erreur : Veuillez remplir tous les champs.");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Erreur : Veuillez remplir tous les champs.");
            alert.show();
            return;
        }

        // Vérifier si l'e-mail est valide
        if (!isValidEmail(tf_email.getText())) {
            // Afficher un message d'erreur si l'e-mail est invalide
            System.out.println("Erreur : Veuillez saisir une adresse e-mail valide.");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Erreur : Veuillez saisir une adresse e-mail valide.");
            alert.show();
            return;
        }

        // Vérifier si l'utilisateur avec cette adresse e-mail existe déjà
        if (userExistsWithEmail(tf_email.getText())) {
            // Afficher un message d'erreur si l'utilisateur existe déjà avec cette adresse e-mail
            System.out.println("Erreur : Un utilisateur avec cette adresse e-mail existe déjà.");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Erreur : Un utilisateur avec cette adresse e-mail existe déjà.");
            alert.show();
            return;
        }

        // Récupérer l'ID de la région sélectionnée dans le ComboBox
        int regionId = getRegionIdByName(cb_region.getValue());

        // Crypter le mot de passe
        String hashedPassword = BCrypt.hashpw(tf_password.getText(), BCrypt.gensalt());

        // Construire le rôle à insérer dans la base de données en fonction des cases à cocher
        String role;
        if (ch_user.isSelected() && ch_admin.isSelected()) {
            role = "[\"ROLE_USER\", \"ROLE_ADMIN\"]";
        } else if (ch_user.isSelected()) {
            role = "[\"ROLE_USER\"]";
        } else {
            role = "[\"ROLE_ADMIN\"]";
        }

        // Le nom d'utilisateur n'existe pas encore et les champs sont valides, procéder à l'insertion
        String query = "INSERT INTO user (email, password, first_name, last_name, adresse, ville, region_id, roles) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, tf_email.getText());
            statement.setString(2, hashedPassword);
            statement.setString(3, tf_firstname.getText());
            statement.setString(4, tf_lastname.getText());
            statement.setString(5, tf_adresse.getText());
            statement.setString(6, tf_ville.getText());
            statement.setInt(7, regionId);
            statement.setString(8, role);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Utilisateur inséré avec succès !");
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Utilisateur inséré avec succès !");
                alert.show();
                showUsers();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'insertion de l'utilisateur", e);
        }
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


    private void updateUser() {
        User selectedUser = table_user.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            String query = "UPDATE user SET ";
            boolean needComma = false;
            boolean roleUpdated = false;
            // Vérifier si l'utilisateur a modifié son email et qu'il est déjà utilisé par un autre utilisateur
            boolean emailAlreadyExists = false;
            if (tf_email.getText() != null && !tf_email.getText().isEmpty() && !tf_email.getText().equals(selectedUser.getEmail())) {
                emailAlreadyExists = userExistsWithEmail(tf_email.getText());
                if (emailAlreadyExists) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Email already exists!");
                    alert.show();
                    return;
                }
            }
            if (tf_password.getText() != null && !tf_password.getText().isEmpty() && !tf_password.getText().equals(selectedUser.getPassword())) {
                query += "password = ?";
                needComma = true;
            }
            if (tf_firstname.getText() != null && !tf_firstname.getText().isEmpty() && !tf_firstname.getText().equals(selectedUser.getFirstName())) {
                query += (needComma ? ", " : "") + "first_name = ?";
                needComma = true;
            }
            if (tf_lastname.getText() != null && !tf_lastname.getText().isEmpty() && !tf_lastname.getText().equals(selectedUser.getLastName())) {
                query += (needComma ? ", " : "") + "last_name = ?";
                needComma = true;
            }
            if (tf_email.getText() != null && !tf_email.getText().isEmpty() && !tf_email.getText().equals(selectedUser.getEmail())) {
                query += (needComma ? ", " : "") + "email = ?";
                needComma = true;
            }
            if (tf_adresse.getText() != null && !tf_adresse.getText().isEmpty() && !tf_adresse.getText().equals(selectedUser.getAdresse())) {
                query += (needComma ? ", " : "") + "adresse = ?";
                needComma = true;
            }
            if (tf_ville.getText() != null && !tf_ville.getText().isEmpty() && !tf_ville.getText().equals(selectedUser.getVille())) {
                query += (needComma ? ", " : "") + "ville = ?";
                needComma = true;
            }
//            if (ch_admin.isSelected() || ch_user.isSelected()) {
//                query += (needComma ? ", " : "") + "roles = ?";
//                roleUpdated = true;
//                needComma = true;
//            }


            if (cb_region.getValue() != null && !cb_region.getValue().isEmpty() && !cb_region.getValue().equals(selectedUser.getRegion().getName())) {
                query += (needComma ? ", " : "") + "region_id = ?";
                needComma = true;
            }

            query += " WHERE id = ?";

            if (needComma) {
                try (Connection conn = getConnection();
                     PreparedStatement statement = conn.prepareStatement(query)) {
                    int parameterIndex = 1;
//                    if (roleUpdated) {
//                        statement.setString(parameterIndex++, (ch_admin.isSelected() && ch_user.isSelected()) ? "[\"ROLE_USER\", \"ROLE_ADMIN\"]" : (ch_admin.isSelected() ? "[\"ROLE_ADMIN\"]" : "[\"ROLE_USER\"]"));
//                    }
                    if (tf_password.getText() != null && !tf_password.getText().isEmpty() && !tf_password.getText().equals(selectedUser.getPassword())) {
                        statement.setString(parameterIndex++, tf_password.getText());
                    }
                    if (tf_firstname.getText() != null && !tf_firstname.getText().isEmpty() && !tf_firstname.getText().equals(selectedUser.getFirstName())) {
                        statement.setString(parameterIndex++, tf_firstname.getText());
                    }
                    if (cb_region.getValue() != null && !cb_region.getValue().isEmpty() && !cb_region.getValue().equals(selectedUser.getRegion().getName())) {
                        statement.setInt(parameterIndex++, getRegionIdByName(cb_region.getValue()));
                    }
                    if (tf_lastname.getText() != null && !tf_lastname.getText().isEmpty() && !tf_lastname.getText().equals(selectedUser.getLastName())) {
                        statement.setString(parameterIndex++, tf_lastname.getText());
                    }
                    if (tf_email.getText() != null && !tf_email.getText().isEmpty() && !tf_email.getText().equals(selectedUser.getEmail())) {
                        statement.setString(parameterIndex++, tf_email.getText());
                    }
                    if (tf_adresse.getText() != null && !tf_adresse.getText().isEmpty() && !tf_adresse.getText().equals(selectedUser.getAdresse())) {
                        statement.setString(parameterIndex++, tf_adresse.getText());
                    }
                    if (tf_ville.getText() != null && !tf_ville.getText().isEmpty() && !tf_ville.getText().equals(selectedUser.getVille())) {
                        statement.setString(parameterIndex++, tf_ville.getText());
                    }
                    statement.setInt(parameterIndex, selectedUser.getId());

                    int rowsUpdated = statement.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Utilisateur mis à jour avec succès !");
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setContentText("Utilisateur mis à jour avec succès !");
                        alert.show();
                        showUsers(); // Actualiser la table des utilisateurs après la mise à jour
                    } else {
                        System.out.println("Aucune modification détectée.");
                        // Ajoutez ici le code pour gérer le cas où aucun champ n'a été modifié
                    }
                } catch (SQLException e) {
                    throw new RuntimeException("Erreur lors de la mise à jour de l'utilisateur", e);
                }
            } else {
                System.out.println("Aucune modification détectée.");
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Warning : Aucune modification détectée.");
                alert.show();
            }
        } else {
            System.out.println("Veuillez sélectionner un utilisateur à mettre à jour.");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Warning : Veuillez sélectionner un utilisateur à mettre à jour.");
            alert.show();
        }
    }


    private void deleteUser() {
        User selectedUser = table_user.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            String query = "DELETE FROM user WHERE id = ?";

            try (Connection conn = getConnection();
                 PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setInt(1, selectedUser.getId());

                int rowsDeleted = statement.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("Utilisateur supprimé avec succès !");
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setContentText("Utilisateur supprimé avec succès !");
                    alert.show();
                    showUsers(); // Actualiser la table des utilisateurs après la suppression
                }
            } catch (SQLException e) {
                throw new RuntimeException("Erreur lors de la suppression de l'utilisateur", e);
            }
        } else {
            System.out.println("Veuillez sélectionner un utilisateur à supprimer.");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Warning : Veuillez sélectionner un utilisateur à supprimer.");
            alert.show();
        }
    }

    private void addCharCountListenerPassword(TextField textField, int place) {
        Label charCountLabel = new Label();
        ImageView validationIcon = new ImageView();

        // Définir la taille de l'icône
        validationIcon.setFitWidth(16); // Largeur de l'icône
        validationIcon.setFitHeight(16); // Hauteur de l'icône

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                // Si newValue est null, le champ de texte est vide, donc pas d'icône ni de texte de compteur de caractères
                validationIcon.setImage(null);
                charCountLabel.setText("");
            } else {
                int length = newValue.length();
                if (length == 0) {
                    validationIcon.setImage(null);
                    charCountLabel.setText("");
                } else if (length <= 2 ) {
                    // Afficher une icône de croix rouge pour indiquer une erreur de saisie
                    validationIcon.setImage(iconInvalid);
                    charCountLabel.setText("Password too short");
                } else {
                    // Vérifier la complexité du mot de passe
                    boolean containsLowercase = newValue.matches(".*[a-z].*");
                    boolean containsUppercase = newValue.matches(".*[A-Z].*");
                    boolean containsDigit = newValue.matches(".*\\d.*");
                    boolean containsSpecial = newValue.matches(".*[!@#$%^&*()-_=+].*");

                    if (containsLowercase && containsUppercase && containsDigit && containsSpecial) {
                        // Mot de passe fort
                        validationIcon.setImage(iconValid);
                        charCountLabel.setText("Strong Password");
                    } else if (containsLowercase && containsUppercase && containsDigit) {
                        // Mot de passe moyen
                        validationIcon.setImage(iconValid);
                        charCountLabel.setText("(Medium Password");
                    } else if (containsLowercase && containsUppercase) {
                        // Mot de passe faible
                        validationIcon.setImage(iconInvalid);
                        charCountLabel.setText("Weak Password");
                    } else {
                        // Mot de passe très faible
                        validationIcon.setImage(iconInvalid);
                        charCountLabel.setText("Very Weak Password");
                    }
                }
            }
            // Mettre à jour le texte du label avec le nombre de caractères saisis
        });
        HBox container = new HBox(charCountLabel, validationIcon);
        container.setSpacing(10); // Ajoutez un espace entre le label et l'icône

        // Récupérer le parent du TextField
        Parent parent = textField.getParent();
        if (parent instanceof AnchorPane) {
            AnchorPane parentContainer = (AnchorPane) parent;
            // Ajouter le conteneur HBox à l'AnchorPane parent du champ TextField
            parentContainer.getChildren().add(container);
            // Réglez le positionnement du conteneur au-dessus du champ TextField
            AnchorPane.setTopAnchor(container, (double) place); // Ajustez la position en haut du conteneur au besoin
        } else if (parent instanceof VBox) {
            // Si le parent est un VBox, ajoutez le conteneur HBox directement au VBox
            VBox vBoxParent = (VBox) parent;
            vBoxParent.getChildren().add(container);
            VBox.setMargin(container, new Insets(place, 0, 0, 0)); // Ajustez les marges au besoin

        }
    }


}
