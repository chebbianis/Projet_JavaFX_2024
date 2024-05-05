package com.example.pidevjava.Contollers;
import com.example.pidevjava.DataBase.MyConnection;
import com.example.pidevjava.Entities.Voyageur;
import com.example.pidevjava.service.serviceVoyageur;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class EtatCivil implements Initializable {
    private Connection connection;

    public EtatCivil() {
        connection = MyConnection.getInstance().getCnx();
    }
    @FXML
    private Button btn_ret;

    @FXML
    private PieChart pieChart;

    public void initialize (URL url, ResourceBundle resourceBundle) {

        btn_ret.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/pidevjava/Voyageur.fxml"));
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



        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Single", 0),
                new PieChart.Data("Married", 0),
                new PieChart.Data("Divorced", 0),
                new PieChart.Data("Widowed", 0)
        );
        pieChart.setData(pieChartData);

        // Appeler la méthode de recherche pour obtenir les informations des voyageurs depuis la base de données
        serviceVoyageur service = new serviceVoyageur();
        try {
            List<Voyageur> voyageurs = service.rechercherAll(); // Appel à la méthode rechercherAll

            // Mettre à jour les données du PieChart avec les informations des voyageurs
            for (Voyageur voyageur : voyageurs) {
                switch (voyageur.getEtat_civil()) {
                    case "Single":
                        pieChartData.get(0).setPieValue(pieChartData.get(0).getPieValue() + 1);
                        break;
                    case "Married":
                        pieChartData.get(1).setPieValue(pieChartData.get(1).getPieValue() + 1);
                        break;
                    case "Divorced":
                        pieChartData.get(2).setPieValue(pieChartData.get(2).getPieValue() + 1);
                        break;
                    case "Widowed":
                        pieChartData.get(3).setPieValue(pieChartData.get(3).getPieValue() + 1);
                        break;
                    default:
                        break;
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Mettre à jour le PieChart avec les nouvelles valeurs
        pieChart.setData(pieChartData);
      //  serviceVoyageur service = new serviceVoyageur();
        try {
            List<Voyageur> voyageurs = service.rechercherAll(); // Appel à la méthode rechercherAll

            // Update the PieChart with the new data and display percentages
            updatePieChartWithData(pieChart, voyageurs);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public static void updatePieChartWithData(PieChart pieChart, List<Voyageur> voyageurs) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        int totalVoyageurs = voyageurs.size();
        int singleCount = 0;
        int marriedCount = 0;
        int divorcedCount = 0;
        int widowedCount = 0;

        for (Voyageur voyageur : voyageurs) {
            switch (voyageur.getEtat_civil()) {
                case "Single":
                    singleCount++;
                    break;
                case "Married":
                    marriedCount++;
                    break;
                case "Divorced":
                    divorcedCount++;
                    break;
                case "Widowed":
                    widowedCount++;
                    break;
            }
        }

        pieChartData.add(new PieChart.Data("Single", calculatePercentage(singleCount, totalVoyageurs)));
        pieChartData.add(new PieChart.Data("Married", calculatePercentage(marriedCount, totalVoyageurs)));
        pieChartData.add(new PieChart.Data("Divorced", calculatePercentage(divorcedCount, totalVoyageurs)));
        pieChartData.add(new PieChart.Data("Widowed", calculatePercentage(widowedCount, totalVoyageurs)));

        // Set the percentage as labels for each PieChart.Data
        for (PieChart.Data data : pieChartData) {
            data.setName(data.getName() + " (" + String.format("%.1f", data.getPieValue()) + "%)");
        }

        pieChart.setData(pieChartData);
    }

    private static double calculatePercentage(int count, int total) {
        return count > 0 ? ((double) count / total) * 100 : 0;
    }
}