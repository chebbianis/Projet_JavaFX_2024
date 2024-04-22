package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

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

import java.util.ResourceBundle;

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
    private TableColumn<?, ?> marque;

    @FXML
    private TableColumn<?, ?> annee;

    @FXML
    private TableColumn<?, ?> prix_j;

    @FXML
    private TableColumn<?, ?> kilometrage;

    @FXML
    private TableColumn<?, ?> nbrPlaces;




    private Stage stage;
    private Scene scene;
    private Parent root;

    VoitureService sv = new VoitureService();



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
    public void initialize(URL url, ResourceBundle rb) {
        try {
            showRec();
            //searchRec();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public ObservableList<Voiture> getVoitureList() throws SQLException {
        ObservableList<Voiture> voitureList = FXCollections.observableArrayList();

        try {
            voitureList.addAll(sv.recuperer());
            System.out.println(voitureList);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return voitureList;
    }

    public void showRec() throws SQLException {

        ObservableList<Voiture> list = getVoitureList();
        marque.setCellValueFactory(new PropertyValueFactory<>("marque"));
        annee.setCellValueFactory(new PropertyValueFactory<>("annee"));
        prix_j.setCellValueFactory(new PropertyValueFactory<>("prix_j"));
        kilometrage.setCellValueFactory(new PropertyValueFactory<>("kilometrage"));
        nbrPlaces.setCellValueFactory(new PropertyValueFactory<>("nbrPlaces"));


        tableviewVoiture.setItems(list);

    }


}
