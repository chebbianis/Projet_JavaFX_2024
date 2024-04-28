package controllers;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;

public class Payment {

    @FXML
    private TextField cardNumberInput;
    @FXML
    private TextField mmInput;
    @FXML
    private TextField yyInput;
    @FXML
    private TextField cvcInput;
    @FXML
    private TextField zipInput;
    private boolean validateInputs() {
        // Validate Card Number
        if (!cardNumberInput.getText().matches("\\d{13,19}")) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Card number must be between 13 and 19 digits.");
            return false;
        }

        // Validate Expiration Date
        try {
            int mm = Integer.parseInt(mmInput.getText());
            int yy = Integer.parseInt(yyInput.getText()) + 2000; // Assuming input is two digits
            LocalDate expiryDate = LocalDate.of(yy, mm, 1);
            if (mm < 1 || mm > 12 || expiryDate.isBefore(LocalDate.now().withDayOfMonth(1))) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Expiration date is invalid or in the past M >12.");
                return false;
            }
        } catch (DateTimeException | NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Expiration date is invalid.");
            return false;
        }

        // Validate CVC
        if (!cvcInput.getText().matches("\\d{3,4}")) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "CVC must be 3 or 4 digits.");
            return false;
        }

        // Validate ZIP Code
        // This is a simple check; consider enhancing based on specific country formats if necessary
        if (zipInput.getText().isEmpty() || !zipInput.getText().matches("\\w{5,10}")) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "ADD min of 5 Numbers.");
            return false;
        }

        return true; // All validations passed
    }

    private void StripeFunction(){
        try {
// Set your secret key here
            Stripe.apiKey = "sk_test_51OpE5HEVwc14cTuMysPOGti5rGQxAtiSNIz4H0aGmBzHfF9f1veh8Z4y1OG7hKi7wwDFxHleG8FJE7KivKHntYlQ00hvK05bID";

// Create a PaymentIntent with other payment details
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(1000L) // Amount in cents (e.g., $10.00)
                    .setCurrency("usd")
                    .build();

            PaymentIntent intent = PaymentIntent.create(params);

// If the payment was successful, display a success message
            System.out.println("Payment successful. PaymentIntent ID: " + intent.getId());
        } catch (StripeException e) {
// If there was an error processing the payment, display the error message
            System.out.println("Payment failed. Error: " + e.getMessage());
        }
    }
    @FXML
    void paiement(ActionEvent event) {
        // Validate input fields first
        if (validateInputs()) {
            // Proceed with payment if validation succeeds
            StripeFunction();
            Platform.runLater(() -> {
                try {
                    // Load the FXML for the home page
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Art/FronClient.fxml"));
                    Parent root = loader.load();

                    // Get the stage from the event
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                    // Set the home page as the new scene
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            });

        } else {
            // Show alert if validation fails
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please check your input fields.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
