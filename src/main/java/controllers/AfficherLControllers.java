package controllers;

import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.io.IOException;

import java.time.LocalDate;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import javafx.stage.Stage;
import javafx.scene.control.ScrollPane;
import javafx.scene.shape.Circle;
import javafx.geometry.Point2D;


public class AfficherLControllers {

    @FXML
    private DatePicker txtDate_debut;

    @FXML
    private DatePicker txtDate_fin;

    @FXML
    private TextField txtUser_id;

    @FXML
    private TextField txtVoiture_id;

    @FXML
    private TextField txtTypePaiement;

    @FXML
    private TextField txtOptions_supp;

    @FXML
    private TextField txtIdL;


    private Stage stage;
    private Scene scene;
    private Parent root;


    public void setTxtDate_debut(LocalDate txtDate_debut) {
        this.txtDate_debut.setValue(txtDate_debut);
    }

    public void setTxtDate_fin(LocalDate txtDate_fin) {
        this.txtDate_fin.setValue(txtDate_fin);
    }

    public void setTxtUser_id(String txtUser_id) {
        this.txtUser_id.setText(txtUser_id);
    }

    public void setTxtVoiture_id(String txtVoiture_id) {
        this.txtVoiture_id.setText(txtVoiture_id);
    }

    public void setTxtTypePaiement(String txtTypePaiement) {
        this.txtTypePaiement.setText(txtTypePaiement);
    }

    public void setTxtOptions_supp(String txtOptions_supp) {
        this.txtOptions_supp.setText(txtOptions_supp);
    }

    public void setTxtIdL(String txtIdL) {
        this.txtIdL.setText(txtIdL);
    }



    public void goToModification(javafx.event.ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierL.fxml"));

        try {
            Parent root = loader.load();
            ModifierLocationController modifierController = loader.getController();
            modifierController.setTxtDate_debut(txtDate_debut.getValue());
            modifierController.setTxtDate_fin(txtDate_fin.getValue());
            modifierController.setTxtUser_id(txtUser_id.getText());
            modifierController.setTxtVoiture_id(txtVoiture_id.getText());
            modifierController.setTxtTypePaiement(txtTypePaiement.getText());
            modifierController.setTxtOptions_supp(txtOptions_supp.getText());
            modifierController.setTxtIdL(txtIdL.getText());

            // Debug statement to check if root is loaded successfully
            System.out.println("FXML loaded successfully.");

            txtDate_fin.getScene().setRoot(root);
        } catch (IOException e) {
            // Instead of just printing, handle the IOException
            e.printStackTrace();
        }
    }

    private final MapPoint guarage = new MapPoint(36.9002385, 10.186814);

    @FXML
    private ScrollPane mapContainer;

    @FXML
    private VBox address;

    public void initialize() {
        MapView mapView = createMapView();
        address.getChildren().add(mapView);
        VBox.setVgrow(mapView, Priority.ALWAYS);
    }

    private MapView createMapView() {
        MapView mapView = new MapView();
        mapView.setPrefSize(400, 300); // Adjust the preferred size to make the map smaller
        mapView.addLayer(new CustomMapLayer());
        mapView.setZoom(15);
        mapView.flyTo(0, guarage, 0.1);
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
            Point2D point = getMapPoint(guarage.getLatitude(), guarage.getLongitude());
            marker.setTranslateX(point.getX());
            marker.setTranslateY(point.getY());
        }
    }



}
