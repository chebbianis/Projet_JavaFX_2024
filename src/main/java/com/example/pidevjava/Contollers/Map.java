package com.example.pidevjava.Contollers;



import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;
public class Map implements Initializable {
    @FXML
    private WebView webView;


    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (webView != null) {
            WebEngine webEngine = webView.getEngine();
            String apiKey = "UR apiKey";
            String googleMapsUrl = "https://www.google.com/maps/embed/v1/view?key=" + apiKey;
            webEngine.load(googleMapsUrl);
        } else {
            System.err.println("WebView is null. Check your FXML file and controller.");
        }
    }
    }





