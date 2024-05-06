package controllers;

import javafx.scene.web.WebView;
import models.Annonce;

public class MapController {
    public void afficherLocalisationSurMap(Annonce annonce, WebView webView) {
        // Récupérer le nom de la ville de l'annonce
        String ville;
        if (annonce.getMapsLink() != null && !annonce.getMapsLink().isEmpty() && !annonce.getMapsLink().equals("0")) {
            ville = annonce.getMapsLink();
        } else {
            ville = annonce.getVilleA();
        }

        // Construire l'URL de la vue panoramique à 360 degrés avec le nom de la ville
        String urlMap = "https://www.google.com/maps/place/" + ville;

        // Charger la carte dans la WebView
        webView.getEngine().load(urlMap);
    }
}
