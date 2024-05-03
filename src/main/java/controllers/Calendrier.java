package controllers;

import entities.Visit;
import services.ServiceVisit;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import jfxtras.scene.control.agenda.Agenda;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class Calendrier {

    @FXML
    private VBox calendrierBox;
    private ServiceVisit serviceVisite;

    public Calendrier() {
        serviceVisite = new ServiceVisit(); // Initialize serviceVisite in the constructor
    }

    public void initialize() {
        // Create a new instance of the agenda
        Agenda agenda = new Agenda();

        // Apply CSS styles to the agenda
        agenda.getStyleClass().addAll("agenda", "style1");

        // Create a label for the current month
        LocalDate currentDate = LocalDate.now();
        Label monthLabel = new Label(currentDate.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()) + " " + currentDate.getYear());
        monthLabel.getStyleClass().add("month-label");

        // Set styles for the month label
        monthLabel.setStyle("-fx-font-family: DM Sans, sans-serif; -fx-font-size: 16px;  -fx-text-fill: #010133;");

        // Add the month label and the agenda to the VBox
        calendrierBox.getChildren().addAll(monthLabel, agenda);
        calendrierBox.setStyle("-fx-background-color: #6ce3d6;"); // Set background color

        try {
            Collection<Visit> visites = serviceVisite.getAllVisites(); // Get visites using your existing method
            addVisitesToAgenda(visites, agenda);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void addVisitesToAgenda(Collection<Visit> visites, Agenda agenda) {
        for (Visit visite : visites) {
            LocalDate visiteDate = visite.getDateVisit();
            LocalDateTime start = visiteDate.atTime(9, 0); // Set start time at 9 o'clock
            LocalDateTime end = visiteDate.atTime(13, 0); // Set end time at 13 o'clock

            // Create appointment for the visite
            agenda.appointments().add(new Agenda.AppointmentImplLocal()
                    .withStartLocalDateTime(start)
                    .withEndLocalDateTime(end)
                    .withSummary(visite.getNom())); // Use visite name as summary
        }
    }
}