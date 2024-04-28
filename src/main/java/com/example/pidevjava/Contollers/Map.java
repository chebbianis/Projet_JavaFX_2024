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
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
public class Map implements Initializable {
    @FXML
    private VBox adresse;
    @FXML
    private Button btn_ret;

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

        MapView mapView = createMapView();
        adresse.getChildren().add(mapView);
        VBox.setVgrow (mapView, Priority.ALWAYS);
           /* if (webView != null) {
                WebEngine webEngine = webView.getEngine();
                String apiKey = "AIzaSyAvaCuPbQVE6KG0oYMykLBeMClOgtX7XoY";
                String googleMapsUrl = "https://www.google.com/maps/embed/v1/view?key=" + apiKey;
               // String googleMapsUrl = "https://www.youtube.com/watch?v=dHBZRb-tXhc&list=PL6TjelGzBJrWK9Avy3doBfhc_VjKToZ1d&index=23";
                webEngine.load(googleMapsUrl);
            } else {
                System.err.println("WebView is null. Check your FXML file and controller.");
            }*/
        }
    private final MapPoint eiffelPoint = new MapPoint(48.8583701,2.2944813);

    private MapView createMapView(){
        MapView mapView = new MapView();
        mapView.setPrefSize(500, 400);
        mapView.addLayer(new CustomMapLayer());
        mapView.setZoom(15);
        mapView.flyTo(0,eiffelPoint,0.1);
        return mapView;
    }

private class CustomMapLayer extends MapLayer {
        private final Node marker;
        public CustomMapLayer(){
        marker = new Circle(5, Color.RED);
        getChildren().add(marker);

        }

       // @Override
    protected void LayoutLayer() {

            Point2D point = getMapPoint(eiffelPoint.getLatitude(), eiffelPoint.getLongitude());
            marker.setTranslateX(point.getX());
            marker.setTranslateX(point.getY());
    }


}


    }





