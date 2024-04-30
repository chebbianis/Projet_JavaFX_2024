package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import models.Annonce;
import services.AnnonceService;

import java.sql.SQLException;
import java.util.List;

public class AfficherAnnonceController {

    @FXML
    private ListView<String> annonceListView;

    private final AnnonceService annonceService = new AnnonceService();

    @FXML
    public void initialize() throws SQLException {
        List<Annonce> annonces = annonceService.allData();
        for (Annonce annonce : annonces) {
            annonceListView.getItems().add(annonce.getTitre_a());
        }
    }
}
