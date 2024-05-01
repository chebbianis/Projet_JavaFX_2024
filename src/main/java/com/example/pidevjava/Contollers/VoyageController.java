package com.example.pidevjava.Contollers;

import com.example.pidevjava.Entities.Voyage;
import com.example.pidevjava.Contollers.Map;
import com.example.pidevjava.service.serviceVoyage;
import com.example.pidevjava.Contollers.Map;
import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class VoyageController implements Initializable {
    @FXML
    private TableColumn<Voyage, Integer> col_id;
    @FXML
    private TableColumn<Voyage, String> col_programme;
    @FXML
    private TableColumn<Voyage, LocalDate> col_datedepart;
    @FXML
    private TableColumn<Voyage, LocalDate> col_datearrive;
    @FXML
    private TableColumn<Voyage, String> col_prix;
    @FXML
    private TableColumn<Voyage, String> col_destination;
    @FXML
    private TableView<Voyage> table_voyage;
    @FXML
    private DatePicker tf_dateA;
    @FXML
    private DatePicker tf_dated;
    @FXML
    private TextField tf_des;
    @FXML
    private TextField tf_prix;
    @FXML
    private TextField tf_prog;
    @FXML
    private Button btn_delete;
    @FXML
    private Button btn_update;
    @FXML
    private Button btn_insert;
    @FXML
    private Button btn_ret;
    @FXML
    private Button btn_map;
    @FXML
    private VBox adresse;

    private final serviceVoyage serviceVoyage = new serviceVoyage();
    private MapView mapView;
    private final java.util.Map<String, MapPoint> destinationCoordinates = new HashMap<>();
    private final MapPoint eiffelPoint = new MapPoint(48.8583701, 2.2944813);
    Map map = new Map();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_programme.setCellValueFactory(new PropertyValueFactory<>("programme"));
        col_datedepart.setCellValueFactory(new PropertyValueFactory<>("dateDepart"));
        col_datearrive.setCellValueFactory(new PropertyValueFactory<>("dateArrive"));
        col_prix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        col_destination.setCellValueFactory(new PropertyValueFactory<>("destination"));

        mapView = createMapView();
        adresse.getChildren().add(mapView);
        VBox.setVgrow(mapView, Priority.ALWAYS);

        loadVoyageData();

        table_voyage.setOnMouseClicked(this::selectVoyage);
        btn_delete.setOnAction(event -> supprimerVoyage());
        btn_update.setOnAction(this::modifierVoyage);
        btn_insert.setOnAction(this::ajouterVoyage);
        btn_map.setOnAction(this::openMap);
        btn_ret.setOnAction(this::retour);

        initializeDestinationCoordinates();
    }

    private void initializeDestinationCoordinates() {
        destinationCoordinates.put("Paris", new MapPoint(48.8588443, 2.2943506));
        destinationCoordinates.put("New York", new MapPoint(40.712776, -74.005974));
        destinationCoordinates.put("London", new MapPoint(51.5074, -0.1278));
        destinationCoordinates.put("Tokyo", new MapPoint(35.6895, 139.6917));
        destinationCoordinates.put("Sydney", new MapPoint(-33.865143, 151.209900));
        destinationCoordinates.put("Rome", new MapPoint(41.9028, 12.4964));
        destinationCoordinates.put("Berlin", new MapPoint(52.5200, 13.4050));
        destinationCoordinates.put("Moscow", new MapPoint(55.7558, 37.6176));
        destinationCoordinates.put("Beijing", new MapPoint(39.9042, 116.4074));
        destinationCoordinates.put("Cairo", new MapPoint(30.0330, 31.2336));
        destinationCoordinates.put("Rio de Janeiro", new MapPoint(-22.9068, -43.1729));
        destinationCoordinates.put("Los Angeles", new MapPoint(34.0522, -118.2437));
        destinationCoordinates.put("Toronto", new MapPoint(43.65107, -79.347015));
        destinationCoordinates.put("Dubai", new MapPoint(25.276987, 55.296249));
        destinationCoordinates.put("Hong Kong", new MapPoint(22.3193, 114.1694));
        destinationCoordinates.put("Bangkok", new MapPoint(13.7563, 100.5018));
        destinationCoordinates.put("Mexico City", new MapPoint(19.4326, -99.1332));
        destinationCoordinates.put("Madrid", new MapPoint(40.4168, -3.7038));
        destinationCoordinates.put("Amsterdam", new MapPoint(52.3676, 4.9041));
        destinationCoordinates.put("Seoul", new MapPoint(37.5665, 126.9780));
        destinationCoordinates.put("Singapore", new MapPoint(1.3521, 103.8198));
        destinationCoordinates.put("Istanbul", new MapPoint(41.0082, 28.9784));
        destinationCoordinates.put("Mumbai", new MapPoint(19.0760, 72.8777));
        destinationCoordinates.put("Lisbon", new MapPoint(38.7223, -9.1393));
        destinationCoordinates.put("Athens", new MapPoint(37.9838, 23.7275));
        destinationCoordinates.put("Stockholm", new MapPoint(59.3293, 18.0686));
        destinationCoordinates.put("Vienna", new MapPoint(48.2082, 16.3738));
        destinationCoordinates.put("Prague", new MapPoint(50.0755, 14.4378));
        destinationCoordinates.put("Copenhagen", new MapPoint(55.6761, 12.5683));
        destinationCoordinates.put("Dublin", new MapPoint(53.3498, -6.2603));
        destinationCoordinates.put("Helsinki", new MapPoint(60.1695, 24.9354));    }

    private void openMap(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/pidevjava/Map.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) btn_map.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void retour(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/pidevjava/hello-view.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) btn_ret.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private MapView createMapView() {
        MapView mapView = new MapView();
        mapView.setPrefSize(500, 400);
        mapView.addLayer(new CustomMapLayer());
        mapView.setZoom(15);
        mapView.flyTo(0, eiffelPoint, 0.1);
        return mapView;
    }

    private class CustomMapLayer extends MapLayer {
        private final Node marker;

        public CustomMapLayer() {
            marker = new Circle(5, Color.RED);
            getChildren().add(marker);
        }

        @Override
        protected void layoutLayer() {
            Point2D point = getMapPoint(eiffelPoint.getLatitude(), eiffelPoint.getLongitude());
            marker.setTranslateX(point.getX());
            marker.setTranslateY(point.getY());
        }
    }

    private void selectVoyage(MouseEvent event) {
        if (event.getClickCount() == 1) {
            Voyage selectedVoyage = table_voyage.getSelectionModel().getSelectedItem();
            if (selectedVoyage != null) {
                tf_prog.setText(selectedVoyage.getProgramme());
                tf_dated.setValue(selectedVoyage.getDateDepart());
                tf_dateA.setValue(selectedVoyage.getDateArrive());
                tf_prix.setText(selectedVoyage.getPrix());
                tf_des.setText(selectedVoyage.getDestination());
                updateMapPosition(selectedVoyage.getDestination());
            }
        }
    }

    private void updateMapPosition(String destination) {
        MapPoint destinationPoint = getDestinationCoordinates(destination);
        if (mapView != null && destinationPoint != null) {
            mapView.flyTo(0, destinationPoint, 0.1);
        }
    }

    private MapPoint getDestinationCoordinates(String destination) {
        return destinationCoordinates.get(destination);
    }
    private ObservableList<Voyage> getVoyageList() {
        ObservableList<Voyage> voyages = FXCollections.observableArrayList();
        try {
            voyages.addAll(serviceVoyage.afficher());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return voyages;
    }

    private void loadVoyageData() {
        ObservableList<Voyage> voyages = getVoyageList();
        table_voyage.setItems(voyages);
    }

    @FXML
    void ajouterVoyage(ActionEvent event) {
        String programmeValue = tf_prog.getText();
        LocalDate dateDepartValue = tf_dated.getValue();
        LocalDate dateArriveValue = tf_dateA.getValue();
        String prixValue = tf_prix.getText();
        String destinationValue = tf_des.getText();
        map.updateMapPosition(destinationValue);

        // Vérification des valeurs saisies
        if (programmeValue.isEmpty() || dateDepartValue == null || dateArriveValue == null || prixValue.isEmpty() || destinationValue.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Ajout impossible", "Veuillez remplir tous les champs.");
            return;
        }

        if (programmeValue.length() > 100) {
            showAlert(Alert.AlertType.WARNING, "Ajout impossible", "Le champ programme ne doit pas dépasser 100 caractères.");
            return;
        }


        if (!isValidDate(dateDepartValue) || !isValidDate(dateArriveValue) || dateDepartValue.isAfter(dateArriveValue)) {
            showAlert(Alert.AlertType.WARNING, "Dates incorrectes", "Les dates doivent être au format jj/mm/aaaa et la date de départ doit être antérieure à la date d'arrivée.");
            return;
        }


        if (!isValidPrice(prixValue)) {
            showAlert(Alert.AlertType.WARNING, "Prix incorrect", "Le champ prix ne doit contenir que des chiffres.");
            return;
        }

        Voyage voyage = new Voyage(programmeValue, dateDepartValue, dateArriveValue, prixValue, destinationValue);

        try {

            serviceVoyage.ajouter(voyage);

            showAlert(Alert.AlertType.CONFIRMATION, "Succès", "Voyage ajouté avec succès.");

            loadVoyageData();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout du voyage : " + e.getMessage());
        }
    }

    @FXML
    void modifierVoyage(ActionEvent event) {
        Voyage voyage = table_voyage.getSelectionModel().getSelectedItem();
        if (voyage != null) {
            String programme = tf_prog.getText();
            LocalDate dateDepart = tf_dated.getValue();
            LocalDate dateArrive = tf_dateA.getValue();
            String prix = tf_prix.getText();
            String destination = tf_des.getText();

            voyage.setProgramme(programme);
            voyage.setDateDepart(dateDepart);
            voyage.setDateArrive(dateArrive);
            voyage.setPrix(prix);
            voyage.setDestination(destination);

            try {
                serviceVoyage.modifier(voyage);
                loadVoyageData();

                showAlert(Alert.AlertType.CONFIRMATION, "Succès", "Voyage modifié avec succès");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucun voyage sélectionné", "Veuillez sélectionner un voyage à modifier.");
        }
    }

    private void supprimerVoyage() {
        Voyage voyage = table_voyage.getSelectionModel().getSelectedItem();
        if (voyage != null) {
            try {
                serviceVoyage.supprimer(voyage);
                loadVoyageData();
                showAlert(Alert.AlertType.INFORMATION, "Suppression réussie", "Le voyage a été supprimé avec succès.");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur lors de la suppression", "Une erreur s'est produite lors de la suppression du voyage : " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucun voyage sélectionné", "Veuillez sélectionner un voyage.");
        }
    }

    private boolean isValidDate(LocalDate date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            formatter.format(date);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean isValidPrice(String price) {
        return price.matches("\\d+");
    }



    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


}
