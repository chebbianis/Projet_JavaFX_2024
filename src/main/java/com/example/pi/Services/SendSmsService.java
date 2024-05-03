package com.example.pi.Services;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import javafx.scene.control.Alert;

public class SendSmsService {
    public static final String ACCOUNT_SID = System.getenv("twilio");
    public static final String AUTH_TOKEN = "5c86c8b5285f24cebd2386aba33c2905";


    public static void sendSMS(String toPhoneNumber, String message) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message.creator(
                        new PhoneNumber("+216"+toPhoneNumber), // To number
                        new PhoneNumber("+15615155806"), // From number
                        message)
                .create();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("SMS successfully sent...");
        alert.show();
    }


}
