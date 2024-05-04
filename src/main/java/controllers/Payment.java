package controllers;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.controlsfx.control.Notifications;
import entities.Reservation;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Payment {

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private Label montantLabel;

    @FXML
    private Button payerButton;

    private Connection connection;
    private Reservation reservation; // Ajout de la référence à la réservation

    public void initialize() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tunvista", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean processPayment() {
        try {
            Stripe.apiKey = "stripe.apiKey=sk_test_51OqgDeBLgOBp2CJ7ampNPipL9yCv3vvR3zvnsIdCda7p4A4tjkLOCuCX5DWNXPuE4B02Xgmom2w85y29EgUU0dHX00qbkhgTuu\n";
            PaymentIntent intent = null;

            try {
                double totalDouble = reservation.getPrix_total();

                if (totalDouble > 500) {
                    totalDouble *= 0.75;
                    Notifications.create()
                            .title("Félicitations!")
                            .text("Vous avez droit à une réduction de 25% sur votre paiement.")
                            .showInformation();
                }

                long amountInCents = (long) (totalDouble * 100);

                PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                        .setAmount(amountInCents)
                        .setCurrency("usd")
                        .build();

                intent = PaymentIntent.create(params);

            } catch (NumberFormatException e) {
                System.err.println("Erreur de conversion : " + e.getMessage());
                return false;
            }

            Notifications.create()
                    .title("Paiement réussi")
                    .text("Le paiement a été effectué avec succès.")
                    .showInformation();

            System.out.println("Payment.fxml successful. PaymentIntent ID: " + intent.getId());
            return true;

        } catch (StripeException e) {
            Notifications.create()
                    .title("Erreur de paiement")
                    .text("Le paiement a échoué. Erreur : " + e.getMessage())
                    .showError();

            System.out.println("Payment.fxml failed. Error: " + e.getMessage());
            return false;
        }
    }
    @FXML
    private TextField numCarteField;


// Dans la méthode payerAction(ActionEvent event)

    @FXML
    void payerAction(ActionEvent event) {
        // Récupérez le numéro de carte depuis le champ de texte
        String numCarte = numCarteField.getText();

        // Vérifiez si le numéro de carte est valide (vous pouvez ajouter une validation plus approfondie si nécessaire)

        // Si le numéro de carte est valide, effectuez le paiement
        if (validateCardNumber(numCarte)) {
            // Créez une alerte de confirmation pour le paiement réussi
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Paiement réussi");
            alert.setHeaderText(null);
            alert.setContentText("Paiement effectué avec succès !");
            alert.showAndWait();

            // Vous pouvez également gérer la réponse de l'utilisateur
            if (alert.getResult() == ButtonType.OK) {
                // L'utilisateur a cliqué sur OK
                System.out.println("Paiement effectué avec succès !");
            } else {
                // L'utilisateur a annulé
                System.out.println("Paiement annulé.");
            }
        } else {
            // Créez une alerte d'erreur pour un numéro de carte invalide
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de paiement");
            alert.setHeaderText(null);
            alert.setContentText("Numéro de carte invalide. Veuillez vérifier et réessayer.");
            alert.showAndWait();

            System.out.println("Numéro de carte invalide. Veuillez vérifier et réessayer.");
        }
    }

    private boolean validateCardNumber(String numCarte) {
        // Vérifiez si le numéro de carte est égal à "4242424242424242"
        return "4242424242424242".equals(numCarte);
    }



    private void afficherMessage(String message) {
        System.out.println(message);
    }

    // Méthode pour définir la réservation
    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
}
