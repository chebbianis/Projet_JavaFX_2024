package test;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.Voiture;

public class MainFX extends Application {



    @Override
    public void start(Stage primaryStage)  throws  Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherL.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("3A45");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
