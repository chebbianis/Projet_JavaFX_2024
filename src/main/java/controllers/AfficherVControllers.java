package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import javafx.event.ActionEvent;
//import java.awt.event.MouseEvent;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Voiture;
import services.VoitureService;

import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.scene.control.Button;


public class AfficherVControllers {

    @FXML
    private TextField txtVoiture_Marque;

    @FXML
    private DatePicker txtAnnee;

    @FXML
    private TextField txtPrix_j;


    @FXML
    private TextField txtId;

    @FXML
    private TextField txtKilometrage;

    @FXML
    private TextField txtNbrPlaces;

    @FXML
    private TableView<Voiture> tableviewVoiture;

    @FXML
    private TableColumn<Voiture, String> colMarque;

    @FXML
    private TableColumn<Voiture, LocalDate> colAnnee;

    @FXML
    private TableColumn<Voiture, Integer> colPrix_j;

    @FXML
    private TableColumn<Voiture, Integer> colKilometrage;

    @FXML
    private TableColumn<Voiture, Integer> colNbrPlaces;

    private VoitureService voitureService;
    private ObservableList<Voiture> voitureList;

    @FXML
    private Button deleteButton;

    @FXML
    private Button updateButton;

    @FXML
    private TextField searchTextField;

    @FXML
    private Button searchButton;



    private Stage stage;
    private Scene scene;
    private Parent root;





    public void setTxtVoiture_Marque(String txtVoiture_Marque) {
        this.txtVoiture_Marque.setText(txtVoiture_Marque);
    }

    public void setTxtAnnee(LocalDate txtAnnee) {
        this.txtAnnee.setValue(txtAnnee);
    }

    public void setTxtPrix_j(String txtPrix_j) {
        this.txtPrix_j.setText(txtPrix_j);
    }

    public void setTxtKilometrage(String txtKilometrage) {
        this.txtKilometrage.setText(txtKilometrage);
    }

    public void setTxtNbrPlaces(String txtNbrPlaces) {
        this.txtNbrPlaces.setText(txtNbrPlaces);
    }

    public void setTxtId(String txtId) {
        this.txtId.setText(txtId);
    }



    public void goToModification(javafx.event.ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierV.fxml"));

        try {
            Parent root = loader.load();
            ModifierVoitureController modifierController = loader.getController();
            modifierController.setTxtAnnee(txtAnnee.getValue());
            modifierController.setTxtVoiture_Marque(txtVoiture_Marque.getText());
            modifierController.setTxtPrix_j(txtPrix_j.getText());
            modifierController.setTxtKilometrage(txtKilometrage.getText());
            modifierController.setTxtNbrPlaceset(txtNbrPlaces.getText());
            modifierController.setTxtId(txtId.getText());

            // Debug statement to check if root is loaded successfully
            System.out.println("FXML loaded successfully.");

            txtAnnee.getScene().setRoot(root);
        } catch (IOException e) {
            // Instead of just printing, handle the IOException
            e.printStackTrace();
        }
    }

    private void selectVoiture(MouseEvent event) {
        if (event.getClickCount() == 1) { // Vérifie si un clic simple a été effectué
            Voiture selectedVoiture = tableviewVoiture.getSelectionModel().getSelectedItem();
            if (selectedVoiture != null) {
                // Affichage des détails du voyage sélectionné dans le formulaire
                txtVoiture_Marque.setText(selectedVoiture.getMarque());
                txtAnnee.setValue(selectedVoiture.getAnnee());
                txtPrix_j.setText(String.valueOf(selectedVoiture.getPrix_j()));
                txtKilometrage.setText(String.valueOf(selectedVoiture.getKilometrage()));
                txtNbrPlaces.setText(String.valueOf(selectedVoiture.getNbrPlaces()));
            }
        }
    }

    public void initialize() {
        voitureService = new VoitureService();
        setupTableView();
        loadVoitures();
    }

    private void setupTableView() {
        colMarque.setCellValueFactory(new PropertyValueFactory<>("marque"));
        colAnnee.setCellValueFactory(new PropertyValueFactory<>("annee"));
        colPrix_j.setCellValueFactory(new PropertyValueFactory<>("prix_j"));
        colKilometrage.setCellValueFactory(new PropertyValueFactory<>("kilometrage"));
        colNbrPlaces.setCellValueFactory(new PropertyValueFactory<>("nbrPlaces"));
    }

    private void loadVoitures() {
        try {
            List<Voiture> voitures = voitureService.recuperer();
            tableviewVoiture.getItems().addAll(voitures);
        } catch (SQLException e) {
            e.printStackTrace(); // Gérer l'erreur
        }
    }


    @FXML
    public void handleRowSelection(MouseEvent event) {
        Voiture selectedVoiture = tableviewVoiture.getSelectionModel().getSelectedItem();
        if (selectedVoiture != null) {
            deleteButton.setDisable(false); // Enable delete button
            updateButton.setDisable(false); // Enable update button
        } else {
            deleteButton.setDisable(true); // Disable delete button if no row is selected
            updateButton.setDisable(true); // Disable update button if no row is selected
        }
    }

    @FXML
    public void handleDeleteButton(ActionEvent event) {
        Voiture selectedVoiture = tableviewVoiture.getSelectionModel().getSelectedItem();
        if (selectedVoiture != null) {
            try {
                // Call your delete method with selectedVoiture.getId() as parameter
                voitureService.supprimer(selectedVoiture.getId_voiture());
                // Remove the selected row from the TableView
                tableviewVoiture.getItems().remove(selectedVoiture);
            } catch (SQLException e) {
                e.printStackTrace(); // Handle error
            }
        }
    }

    public void handleUpdateButton(ActionEvent event) {
        Voiture selectedVoiture = tableviewVoiture.getSelectionModel().getSelectedItem();
        if (selectedVoiture != null) {
            try {
                // Load the ModifierVoiture.fxml file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierV.fxml"));
                Parent root = loader.load();

                // Get the controller for ModifierV.fxml
                ModifierVoitureController modifierController = loader.getController();

                // Pass the selected voiture to the controller to pre-fill fields
                modifierController.setTextFields(selectedVoiture);

                // Create a new stage
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Modifier Voiture");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace(); // Handle error loading FXML
            }

        }
    }

    @FXML
    private void searchVoitures() {
        String searchString = searchTextField.getText().toLowerCase();

        tableviewVoiture.getItems().clear(); // Clear previous items

        try {
            if (searchString.isEmpty()) {
                // If search text is empty, reload all voitures
                loadVoitures();
            } else {
                List<Voiture> filteredVoitures = voitureService.recuperer().stream()
                        .filter(voiture -> voiture.getMarque().toLowerCase().contains(searchString))
                        .collect(Collectors.toList());

                tableviewVoiture.getItems().addAll(filteredVoitures);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the SQLException
            // Optionally, display an error message to the user
        }
    }


}
