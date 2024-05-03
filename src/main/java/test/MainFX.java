package test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import static controllers.AjouterVisit.init;


public class MainFX extends Application {


    @Override
    public void start(Stage primaryStage)  throws  Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterMaison.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("BIENVENUE");
        primaryStage.setScene(scene);
        primaryStage.show();
        init();
      //  Main.checkVisitsForReminder();


    }

    public static void main(String[] args) {
        launch(args);

    }
}
