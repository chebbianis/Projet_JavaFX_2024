package com.example.pi.Controllers;

import com.example.pi.DB.DBUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class LoggedInController implements Initializable {
    @FXML
    private Button button_logout;

    @FXML
    private Label label_welcome;

    @FXML
    Label label_okhrej;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        button_logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event,"/com/example/pi/hello-view.fxml","Login",null);
            }
        });
    }

    public void setUserInformation(String username){
        label_welcome.setText("Welcome "+ username + "!");
    }
}
