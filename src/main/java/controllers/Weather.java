package controllers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ResourceBundle;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class Weather {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField city;

    @FXML
    private Text currentTemp;

    @FXML
    private Text felt_temp;

    @FXML
    private Button getWeather;

    @FXML
    private Text pressure;

    @FXML
    private Text wind;

    @FXML
    void initialize() {
        getWeather.setOnAction(event -> {
            String usercity = city.getText().trim();
            if (!usercity.isEmpty()) {
                String urlAdress = "http://api.weatherapi.com/v1/current.json?key=805f609064bc43ec8d2105338241502%20&q=" + usercity + "&aqi=no";
                String output = getContent(urlAdress);

                if (output != null) {
                    JSONParser parser = new JSONParser();
                    try {
                        Object obj = parser.parse(output);
                        JSONObject jsonObject = (JSONObject) obj;

                        JSONObject current = (JSONObject) jsonObject.get("current");
                        double tempC = (double) current.get("temp_c");
                        double feelsLikeC = (double) current.get("feelslike_c");
                        double windKph = (double) current.get("wind_kph");
                        double pressureMb = (double) current.get("pressure_mb");

                        currentTemp.setText(String.valueOf(tempC));
                        felt_temp.setText(String.valueOf(feelsLikeC));
                        wind.setText(String.valueOf(windKph));
                        pressure.setText(String.valueOf(pressureMb));
                    } catch (ParseException e) {
                        System.out.println("Erreur lors de l'analyse du JSON : " + e.getMessage());
                    }
                } else {
                    System.out.println("Le contenu de l'API est vide.");
                }
            }
        });
    }

    private static String getContent(String urlAdress) {
        StringBuilder content = new StringBuilder();

        try {
            URL url = new URL(urlAdress);
            URLConnection urlConnection = url.openConnection();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            System.out.println("Une erreur s'est produite lors de la récupération des données de l'API : " + e.getMessage());
            return null;
        }
        return content.toString();
    }
}