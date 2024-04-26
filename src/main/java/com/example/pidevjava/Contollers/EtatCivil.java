package com.example.pidevjava.Contollers;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;

import java.net.URL;
import java.util.ResourceBundle;

public class EtatCivil {


    @FXML
    private PieChart pieChart;

    public void initializee(URL url, ResourceBundle resourceBundle) {
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Single", 2),
                        new PieChart.Data("Married", 25),
                        new PieChart.Data("Divorced", 50),
                        new PieChart.Data("Widowed", 3));


        pieChartData.forEach(data ->
                data.nameProperty().bind(
                        Bindings.concat(
                                data.getName(), " amount: ", data.pieValueProperty()
                        )
                )
        );

        pieChart.getData().addAll(pieChartData);
    }
}
