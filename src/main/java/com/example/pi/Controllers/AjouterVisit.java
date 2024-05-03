package com.example.pi.Controllers;




import com.example.pi.Entities.EmailSender;
import com.example.pi.Entities.Visit;
import com.example.pi.Services.ServiceVisit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;


public class AjouterVisit {

    @FXML
    private Label comboVLabel;

    @FXML
    private DatePicker dateV;

    public Label getComboVLabel() {
        return comboVLabel;
    }

    public void setComboVLabel(Label comboVLabel) {
        this.comboVLabel = comboVLabel;
    }

    public DatePicker getDateV() {
        return dateV;
    }

    public void setDateV(DatePicker dateV) {
        this.dateV = dateV;
    }

    public Label getEmailVLabel() {
        return emailVLabel;
    }

    public void setEmailVLabel(Label emailVLabel) {
        this.emailVLabel = emailVLabel;
    }

    public Label getNomVLabel() {
        return nomVLabel;
    }

    public void setNomVLabel(Label nomVLabel) {
        this.nomVLabel = nomVLabel;
    }

    public TextField getNumV() {
        return numV;
    }

    public void setNumV(TextField numV) {
        this.numV = numV;
    }

    @FXML
    private Label emailVLabel;

    @FXML
    private Label nomVLabel;

    @FXML
    private TextField numV;

    @FXML
    private Label disponibiliteLabel;

    private Connection connection;
    private Visit visit;

    LocalDate today = LocalDate.now();
    public static void init() {
        LocalDate today = LocalDate.now();
        rappel(today);
    }

    public static void rappel(LocalDate today)
    {     ServiceVisit serviceVisit =new ServiceVisit();

        try {
            List<Visit> visits= serviceVisit.afficher();
            for ( Visit v :visits) {
                if (v.getDateVisit().equals(today.plusDays(1))) {
                    EmailSender.sendWelcomeEmailWithSignature(v.getEmail(), v.getNom());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    private void initialize() {
        populateComboBox();
        dateV.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String selectedNomMaison = comboVLabel.getText();
                int ref_b = 0;
                try {
                    ref_b = getNomMaison(selectedNomMaison);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                int demandesRestantes = getDemandesRestantes(newValue, ref_b);
                if (demandesRestantes == -1) {
                    disponibiliteLabel.setText("Erreur de vérification de la disponibilité");
                } else if (demandesRestantes <= 0) {
                    disponibiliteLabel.setText("Maison indisponible pour cette date");
                } else if (demandesRestantes <= 3) {
                    disponibiliteLabel.setText("Attention ! Il reste seulement " + demandesRestantes + " demande(s) disponible(s) pour cette date.");
                } else {
                    disponibiliteLabel.setText("");
                }
            }
        });
    }

    @FXML
    private void ajouterVisit(ActionEvent event) {
        try {
            if (numV.getText().isEmpty()  || dateV.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez remplir tous les champs.");
                alert.showAndWait();
            } else {
                int numero = Integer.parseInt(numV.getText());
                LocalDate date_visit = dateV.getValue();
                String selectedNomMaison = comboVLabel.getText();
                int ref_b = getNomMaison(selectedNomMaison);

                LocalDate dateVisit = dateV.getValue();
                LocalDate today = LocalDate.now();
                if (dateVisit.isBefore(today)) {
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Wrong date");
                    successAlert.setHeaderText("Veuillez sélectionner les dates à partir d'aujourd'hui");
                    successAlert.showAndWait();
                    return;
                }

                String adresseEmailValue = emailVLabel.getText();
             /*   if (!adresseEmailValue.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Entrée invalide");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Veuillez saisir une adresse e-mail valide.");
                    errorAlert.showAndWait();
                    return;
                }*/
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
                visit.setNom(nomVLabel.getText());
                visit.setEmail(emailVLabel.getText());
                visit.setRefB(ref_b);
                visit.setDateVisit(date_visit);

                String req = "INSERT INTO visit (numero, nom, email, refB, date_visit) VALUES (?, ?, ?, ?, ?)";
                try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tunvista_integration", "root", "mohamedomar");
                     PreparedStatement preparedStatement = connection.prepareStatement(req)) {
                    preparedStatement.setInt(1, visit.getNumero());
                    preparedStatement.setString(2, visit.getNom());
                    preparedStatement.setString(3, visit.getEmail());
                    preparedStatement.setInt(4, visit.getRefB());
                    preparedStatement.setDate(5, java.sql.Date.valueOf(visit.getDateVisit()));
                    int rowsAffected = preparedStatement.executeUpdate();
                    clear();

                    if (rowsAffected > 0) {
                      /*  if (visit.getDateVisit().equals(today.plusDays(1))) {
                            EmailSender.sendWelcomeEmailWithSignature(visit.getEmail(), visit.getNom());
                        }*/
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
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tunvista_integration", "root", "mohamedomar");
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
            String url = "jdbc:mysql://localhost:3306/tunvista_integration";
            String username = "root";
            String password = "mohamedomar";
            Connection connection = DriverManager.getConnection(url, username, password);
            String sql = "SELECT ref_b, nom FROM maison";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String nomMaison = resultSet.getString("nom");
                nomMaisonList.add(nomMaison);
            }
            comboVLabel.setText(nomMaisonList.get(0)); // Set the first item as default
            resultSet.close();

        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }

    private int getDemandesRestantes(LocalDate dateVisit, int refB) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tunvista_integration", "root", "mohamedomar");
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) AS nombreVisites FROM visit WHERE date_visit = ? AND refB = ?")) {
            preparedStatement.setDate(1, java.sql.Date.valueOf(dateVisit));
            preparedStatement.setInt(2, refB);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int nombreVisites = resultSet.getInt("nombreVisites");
                    return 5 - nombreVisites;
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
        return -1; // En cas d'erreur
    }

    private void clear() {
        nomVLabel.setText("");
        numV.setText(null);
        emailVLabel.setText("");
        comboVLabel.setText("");
        dateV.setValue(null);
    }
}
