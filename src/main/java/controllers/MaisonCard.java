package controllers;

import entities.Maison;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class MaisonCard {

    @FXML
    private Text TeamLabel;

    @FXML
    private ImageView imageView;

    @FXML
    private Text projectName;
    @FXML
    private AnchorPane cardProject;
    private Maison maison;

    public void setProjectCard(Maison maison){
        this.maison=maison;
        projectName.setText(maison.getNom());
       // projectDescription.setText(maison.getProject_description());
        TeamLabel.setText(String.valueOf(maison.getPrix()));
    }



}
