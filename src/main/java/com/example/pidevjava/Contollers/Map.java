package com.example.pidevjava.Contollers;
import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class Map implements Initializable {
    @FXML
    private VBox adresse;
    @FXML
    private Button btn_ret;
    private MapView mapView;
    private final java.util.Map<String, MapPoint> destinationCoordinates = new HashMap<>();
    private final MapPoint eiffelPoint = new MapPoint(48.8583701, 2.2944813);

    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_ret.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/pidevjava/Voyage.fxml"));
                    Parent root = fxmlLoader.load();

                    // Create a new scene
                    Scene scene = new Scene(root);

                    // Get the stage from the button's scene
                    Stage stage = (Stage) btn_ret.getScene().getWindow();

                    // Set the new scene
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        mapView = createMapView();
        adresse.getChildren().add(mapView);
        VBox.setVgrow(mapView, Priority.ALWAYS);
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
    public Map() {
        // Pré-remplir la liste des coordonnées pour quelques destinations
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
        destinationCoordinates.put("Helsinki", new MapPoint(60.1695, 24.9354));
    }

    public void updateMapPosition(String destination) {
        MapPoint destinationPoint = getDestinationCoordinates(destination);
        if (mapView != null && destinationPoint != null) {
            mapView.flyTo(0, destinationPoint, 0.1);
        }
    }

    private MapPoint getDestinationCoordinates(String destination) {
        return destinationCoordinates.get(destination);
    }
}
