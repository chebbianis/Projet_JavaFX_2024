package controllers;

import com.github.sarxos.webcam.Webcam;
import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.sun.java.accessibility.util.Translator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import models.Annonce;
import models.User;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.objdetect.CascadeClassifier;
import services.AnnonceService;
import services.UserService;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;






public class AnnonceController {
    @FXML
    private Button btn_ajouter;
    @FXML
    private Button btn_modifier;
    @FXML
    private Button btn_supprimer;
    @FXML
    private TextField tf_titre_a;
    @FXML
    private TextField tf_description_a;
    @FXML
    private ComboBox<String> tf_ville_a;
    @FXML
    private DatePicker tf_date_debut_a;
    @FXML
    private TextField tf_date_fin_a;
    @FXML
    private TextField tf_nb_jour_a;
    @FXML
    private TextField tf_user_id;
    @FXML
    private TextField tf_maps_link;
    @FXML
    private TableView<Annonce> tvAnnonces;
    @FXML
    private TableColumn<Annonce, String> colIdAnnonce;
    @FXML
    private TableColumn<Annonce, String> colTitreAnnonce;
    @FXML
    private TableColumn<Annonce, String> colDescriptionAnnonce;
    @FXML
    private TableColumn<Annonce, String> colVilleAnnonce;
    @FXML
    private TableColumn<Annonce, LocalDate> colDateDebutA;
    @FXML
    private TableColumn<Annonce, LocalDate> colDateFinA;
    @FXML
    private TableColumn<Annonce, String> colNbJourA;
    @FXML
    private TableColumn<Annonce, String> colUserId;
    @FXML
    public TableColumn<Annonce, String> colMapsLink;
    @FXML
    private TextField tf_search;
    @FXML
    private VBox adresse;

    @FXML
    private ImageView qrCodeImageView;
    private final Map<String, String> cityCoordinates = new HashMap<>();

    private final AnnonceService AnnonceService = new AnnonceService();
    private final UserService UserService = new UserService();
    @FXML
    private WebView webView360; // Make sure to match this with the ID in your FXML file

    @FXML
    private ImageView webcamView;
    private final ObservableList<String> cityList = FXCollections.observableArrayList();
    @FXML
    private Button statistiqueButton;
    @FXML
    private Button btn_generate_qr;
    @FXML
    private Button exportButton;
    private final ObservableList<String> cities = FXCollections.observableArrayList();
    @FXML
    private TextArea inputTextArea;

    @FXML
    private TextArea outputTextArea;

    private Translator translator;
    @FXML
    private ImageView imageView;

    private Stage stage;
    @FXML
    private Pagination pagination;

    private int itemsPerPage = 14;
    @FXML
    private MapView mapView;





    public void initialize() throws SQLException {
        initializeCityCoordinates();
        // Initialisation des colonnes et chargement des données
        colIdAnnonce.setCellValueFactory(new PropertyValueFactory<>("idAnnonce"));
        colTitreAnnonce.setCellValueFactory(new PropertyValueFactory<>("titreA"));
        colDescriptionAnnonce.setCellValueFactory(new PropertyValueFactory<>("descriptionA"));
        colVilleAnnonce.setCellValueFactory(new PropertyValueFactory<>("villeA"));
        colDateDebutA.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        colNbJourA.setCellValueFactory(new PropertyValueFactory<>("nbJour"));
        colDateFinA.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        colUserId.setCellValueFactory(new PropertyValueFactory<>("user"));
        colMapsLink.setCellValueFactory(new PropertyValueFactory<>("mapsLink"));




        tf_date_debut_a.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateDateFin(newValue, tf_nb_jour_a.getText());
        });
        tf_nb_jour_a.textProperty().addListener((observable, oldValue, newValue) -> {
            updateDateFin(tf_date_debut_a.getValue(), newValue);
        });

        tf_search.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                searchAnnonce(newValue);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        //tf_search.setTextFormatter(textFormatter);

        String userId = "100";
        loadAnnonceData(); // Chargement des données dans la TableView
        loadUserAndGenerateQR(userId);

        // Ajout de l'événement pour sélectionner une annonce dans le formulaire
        //tvAnnonces.setOnMouseClicked(event -> selectAnnonce(event));

        btn_supprimer.setOnAction(event -> supprimerAnnonce());
        btn_modifier.setOnAction(event -> modifierAnnonce(event));
        btn_ajouter.setOnAction(event -> {
            try {
                ajouterAnnonce(event);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        });
//        public void setStage(Stage stage) {
//            this.stage = stage;
//        }
        tvAnnonces.setOnMouseClicked(event -> {
            Annonce selectedAnnonce = tvAnnonces.getSelectionModel().getSelectedItem();
            if (selectedAnnonce != null) {
                handleAnnouncementClick(selectedAnnonce);
            }
        });
        String imageUrl360 = "https://www.google.com/maps/@latitude,longitude,fov=80,horizontal_angle=90,vertical_angle=0,tilt=0,pitch=0"; // Replace this URL with the actual URL of your 360-degree image
        webView360.getEngine().load(imageUrl360);





        mapView = createMapView();
        adresse.getChildren().add(mapView);
        VBox.setVgrow(mapView, Priority.ALWAYS);
        cityList.addAll(
                "Ariana", "Béja", "Ben Arous", "Bizerte", "Gabès", "Gafsa",
                "Jendouba", "Kairouan", "Kasserine", "Kébili", "Le Kef", "Mahdia",
                "Manouba", "Medenine", "Monastir", "Nabeul", "Sfax", "Sidi Bouzid",
                "Siliana", "Sousse", "Tataouine", "Tozeur", "Tunis", "Zaghouan"
        );
        tf_ville_a.setItems(cityList);



        // Définissez le nombre total d'éléments dans votre TableView
        int totalItems = getAnnonceList().size();
        int pageCount = (int) Math.ceil((double) totalItems / itemsPerPage);
        pagination.setPageCount(pageCount);
        pagination.setCurrentPageIndex(0);
        updateTableView(0); // Display the first page initially

        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            try {
                updateTableView((int) newIndex);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });



    }

    public void afficherLocalisationSurMap(Annonce annonce) {
        MapController mapController = new MapController();
        mapController.afficherLocalisationSurMap(annonce, webView360);
    }
    private void loadUserAndGenerateQR(String userId) {
        // Database connection parameters
        String url = "jdbc:mysql://localhost:3306/tunvista";
        String username = "root";
        String password = "";

        // SQL query to fetch user details based on user ID
        String query = "SELECT email, first_name, last_name, adresse, ville FROM user WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Set the user ID parameter in the prepared statement
            statement.setString(1, userId);

            // Execute the query
            try (ResultSet resultSet = statement.executeQuery()) {
                // Check if user with the given ID exists
                if (resultSet.next()) {
                    // Retrieve user details from the result set
                    String email = resultSet.getString("email");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    String address = resultSet.getString("adresse");
                    String city = resultSet.getString("ville");

                    // Format user information as a string
                    String userInfo = String.format("Email: %s\nFirst Name: %s\nLast Name: %s\nAddress: %s\nCity: %s",
                            email, firstName, lastName, address, city);

                    // Generate QR code
                    generateQRCode(userInfo);
                } else {
                    // User with the given ID does not exist
                    System.out.println("User not found");
                }
            } catch (WriterException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database connection or query errors
        }
    }

//    private BufferedImage generateQRCode(String adData) {
//        return generateQRCodeImage(adData);
//    }

    private void displayQRCode(BufferedImage qrCodeImage) {
        //qrCodeContainer.getChildren().clear();
        ImageView imageView = new ImageView(String.valueOf(qrCodeImage));
       // qrCodeContainer.getChildren().add(imageView);
    }

    // This method is called when an ad is clicked
    @FXML
    private void handleAdClick() {
        String userId = "100";
        // Load the ad and generate its QR code when the ad is clicked
        loadUserAndGenerateQR(userId);
    }

//    private BufferedImage generateQRCode(String adData) {
//        return generateQRCodeImage(adData);
//    }

    @FXML
    private void generateQRCode() {
        String name = "Ali";
        String email = "ali@gmail.com";

        // Concatenate user information
        String userInfo = "Name: " + name + "\n" + "Email: " + email;

        // Generate QR Code
        ByteArrayOutputStream out = QRCode.from(userInfo).to(ImageType.PNG).stream();
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        qrCodeImageView.setImage(new Image(in));
    }
//@FXML
//private void generateQRCode(ActionEvent event) {
//    try {
//        // Get coordinates from some source
//        double latitude = getLatitudeFromSource();
//        double longitude = getLongitudeFromSource();
//
//        // Convert coordinates to a string
//        String coordinates = String.format("Latitude: %.6f, Longitude: %.6f", latitude, longitude);
//
//        // Generate QR code
//        BufferedImage qrCodeImage = generateQRCode(coordinates);
//
//        // Display QR code
//        Image qrCodeFXImage = SwingFXUtils.toFXImage(qrCodeImage, null);
//        qrCodeImageView.setImage(qrCodeFXImage);
//    } catch (NumberFormatException e) {
//        // Handle invalid input
//        e.printStackTrace();
//    }
//}

    private double getLatitudeFromSource() {
        // Implement code to get latitude from some source
        return 0.0; // Placeholder value
    }

    private double getLongitudeFromSource() {
        // Implement code to get longitude from some source
        return 0.0; // Placeholder value
    }
    @FXML
    private void generateQRCode(ActionEvent event) {
        String url = "jdbc:mysql://localhost:3306/tunvista";
        String username = "root";
        String password = "";

        // SQL query to fetch user details based on user ID
        String query = "SELECT email, first_name, last_name, adresse, ville FROM user WHERE id = ?";

        String userInfo = null;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Set the user ID parameter in the prepared statement
            String userId = tf_user_id.getText();
            statement.setString(1, userId);

            // Execute the query
            try (ResultSet resultSet = statement.executeQuery()) {
                // Check if user with the given ID exists
                if (resultSet.next()) {
                    // Retrieve user details from the result set
                    String email = resultSet.getString("email");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    String address = resultSet.getString("adresse");
                    String city = resultSet.getString("ville");

                    // Format user information as a string
                    userInfo = String.format("Email: %s\nFirst Name: %s\nLast Name: %s\nAddress: %s\nCity: %s",
                            email, firstName, lastName, address, city);

                    // Generate QR code
                    //generateQRCode(userInfo);
                } else {
                    // User with the given ID does not exist
                    System.out.println("User not found");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database connection or query errors
        }
        String message = userInfo;
        try {
            BufferedImage qrCodeImage = generateQRCode(message);
            Image qrCodeFXImage = SwingFXUtils.toFXImage(qrCodeImage, null);
            qrCodeImageView.setImage(qrCodeFXImage);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }


    private BufferedImage generateQRCode(String message) throws WriterException {
        int width = 300;
        int height = 300;
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(message, BarcodeFormat.QR_CODE, width, height, hints);
        BufferedImage qrCodeImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                qrCodeImage.setRGB(x, y, bitMatrix.get(x, y) ? 0x000000 : 0xFFFFFF);
            }
        }
        return qrCodeImage;
    }
//    private BufferedImage generateQRCode(String data) {
//        try {
//            // Create QR Code writer
//            QRCodeWriter qrCodeWriter = new QRCodeWriter();
//
//            // Set QR Code options
//            Map<EncodeHintType, Object> hints = new HashMap<>();
//            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
//
//            // Encode data into a BitMatrix
//            BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 300, 300, hints);
//
//            // Convert BitMatrix to BufferedImage
//            int width = bitMatrix.getWidth();
//            int height = bitMatrix.getHeight();
//            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//            for (int x = 0; x < width; x++) {
//                for (int y = 0; y < height; y++) {
//                    bufferedImage.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
//                }
//            }
//
//            return bufferedImage;
//        } catch (WriterException e) {
//            // Handle QR code generation failure
//            e.printStackTrace();
//            return null;
//        }
//    }
    private void initializeCityCoordinates() {
        cityCoordinates.put("Tunis", "36.806745,10.181392");
        cityCoordinates.put("Ariana", "36.866609,10.164723");
        cityCoordinates.put("Ben Arous", "36.743565,10.231954");
        cityCoordinates.put("La Manouba", "36.809316,10.086341");
        cityCoordinates.put("Nabeul", "36.451232,10.735811");
        cityCoordinates.put("Zaghouan", "36.409067,10.142286");
        cityCoordinates.put("Bizerte", "37.276630,9.864115");
        cityCoordinates.put("Béja", "36.733278,9.184363");
        cityCoordinates.put("Jendouba", "36.507148,8.775573");
        cityCoordinates.put("Le Kef", "36.167908,8.709562");
        cityCoordinates.put("Siliana", "36.088118,9.364283");
        cityCoordinates.put("Kairouan", "35.671018,10.100636");
        cityCoordinates.put("Kasserine", "35.171903,8.830873");
        cityCoordinates.put("Sidi Bouzid", "35.035419,9.483931");
        cityCoordinates.put("Sousse", "35.823978,10.634162");
        cityCoordinates.put("Monastir", "35.764156,10.811427");
        cityCoordinates.put("Mahdia", "35.502247,11.045700");
        cityCoordinates.put("Sfax", "34.739398,10.760217");
        cityCoordinates.put("Gabès", "33.887756,10.097669");
        cityCoordinates.put("Médenine", "33.339856,10.495841");
        cityCoordinates.put("Tataouine", "32.920859,10.450973");
        cityCoordinates.put("Kébili", "33.706912,8.971528");
        cityCoordinates.put("Gafsa", "34.430931,8.775695");
        cityCoordinates.put("Tozeur", "33.918158,8.123013");
    }

    private MapView createMapView(String coordinates) {
        MapView mapView = new MapView();
        mapView.setPrefSize(500, 400);
        mapView.setZoom(18);

        // Split coordinates string to get latitude and longitude
        String[] parts = coordinates.split(",");
        double latitude = Double.parseDouble(parts[0]);
        double longitude = Double.parseDouble(parts[1]);

        // Create a custom map layer with the marker
        CustomMapLayer customMapLayer = new CustomMapLayer();
        customMapLayer.layoutLayer(latitude, longitude);

        // Add the custom map layer to the map
        mapView.addLayer(customMapLayer);

        return mapView;
    }

//    private Shape createMarker() {
//        Circle marker = new Circle(10, Color.RED);
//        marker.setStroke(Color.BLACK);
//        marker.setStrokeWidth(2);
//        return marker;
//    }
private final MapPoint eiffelPoint = new MapPoint(36.806745,10.181392);

    private MapView createMapView(){
        MapView mapView = new MapView();
        mapView.setPrefSize(500, 400);
        mapView.addLayer(new CustomMapLayer());
        mapView.setZoom(15);
        mapView.flyTo(0,eiffelPoint,0.1);
        return mapView;
    }
    private BufferedImage generateQRCodeImage(String text) {
        try {
            // Generate the QR code image
            BitMatrix matrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, 200, 200);
            int width = matrix.getWidth();
            int height = matrix.getHeight();
            BufferedImage qrImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            // Fill in the image with the QR code data
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    qrImage.setRGB(x, y, matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }

            return qrImage;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public class CustomMapLayer extends MapLayer {
        private final Shape marker;

        public CustomMapLayer() {
            marker = createMarker();
            getChildren().add(marker);
        }

        protected void layoutLayer(double latitude, double longitude) {
            Point2D point = getMapPoint(latitude, longitude);
            marker.setTranslateX(point.getX());
            marker.setTranslateY(point.getY());
        }

        private Shape createMarker() {
            Circle marker = new Circle(10, Color.RED);
            marker.setStroke(Color.BLACK);
            marker.setStrokeWidth(2);
            return marker;
        }
    }

    private void handleAnnouncementClick(Annonce annonce) {
        String city = annonce.getVilleA();
        afficherLocalisationSurMap(annonce);

        if (annonce != null) {
            tf_titre_a.setText(annonce.getTitreA());
            tf_description_a.setText(annonce.getDescriptionA());
            tf_ville_a.setValue(annonce.getVilleA());
            tf_date_debut_a.setValue(annonce.getDateDebut());
            tf_date_fin_a.setText(annonce.getDateFin());
            tf_nb_jour_a.setText(annonce.getNbJour());
            tf_user_id.setText(annonce.getUser());
            tf_maps_link.setText(annonce.getMapsLink());

            // Set map center if coordinates are available
            String coordinates = getCoordinatesByCity(city);
            if (coordinates != null) {
                String[] parts = coordinates.split(",");
                double latitude = Double.parseDouble(parts[0]);
                double longitude = Double.parseDouble(parts[1]);
                mapView.setCenter(latitude, longitude);
            } else {
                showAlert(Alert.AlertType.ERROR, "Coordonnées introuvables",
                        "Les coordonnées de la ville sélectionnée sont introuvables.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Annonce introuvable",
                    "L'annonce sélectionnée est introuvable.");
        }
    }



    private String getCoordinatesByCity(String city) {
        switch (city) {
            case "Tunis":
                return "36.806745,10.181392";
            case "Ariana":
                return "36.866609,10.164723";
            case "Ben Arous":
                return "36.743565,10.231954";
            case "Manouba":
                return "36.809316,10.086341";
            case "Nabeul":
                return "36.451232,10.735811";
            case "Zaghouan":
                return "36.409067,10.142286";
            case "Bizerte":
                return "37.276630,9.864115";
            case "Béja":
                return "36.733278,9.184363";
            case "Jendouba":
                return "36.507148,8.775573";
            case "Le Kef":
                return "36.167908,8.709562";
            case "Siliana":
                return "36.088118,9.364283";
            case "Kairouan":
                return "35.671018,10.100636";
            case "Kasserine":
                return "35.171903,8.830873";
            case "Sidi Bouzid":
                return "35.035419,9.483931";
            case "Sousse":
                return "35.823978,10.634162";
            case "Monastir":
                return "35.764156,10.811427";
            case "Mahdia":
                return "35.502247,11.045700";
            case "Sfax":
                return "34.739398,10.760217";
            case "Gabès":
                return "33.887756,10.097669";
            case "Médenine":
                return "33.339856,10.495841";
            case "Tataouine":
                return "32.920859,10.450973";
            case "Kébili":
                return "33.706912,8.971528";
            case "Gafsa":
                return "34.430931,8.775695";
            case "Tozeur":
                return "33.918158,8.123013";
            default:
                return null;
        }
    }





    // Update the map view with the new location
    private void updateMapView(String mapsLink) {
        MapView mapView = createMapView(mapsLink);
        adresse.getChildren().clear(); // Clear existing map
        adresse.getChildren().add(mapView);
        VBox.setVgrow(mapView, Priority.ALWAYS);
    }

    private void updateDateFin(LocalDate dateDebut, String nbJour) {
        if (dateDebut != null && !nbJour.isEmpty()) {
            try {
                int nbJourInt = Integer.parseInt(nbJour);
                String dateFin = String.valueOf(dateDebut.plusDays(nbJourInt));
                tf_date_fin_a.setText(dateFin);
            } catch (NumberFormatException e) {
                // Handle invalid nbJour input
                tf_date_fin_a.setText(null);
            }
        } else {
            tf_date_fin_a.setText(null);
        }
    }


    private void selectAnnonce(Annonce selectedAnnonce) {
        if (selectedAnnonce != null) {
            // Affichage des détails de l'annonce sélectionnée dans le formulaire
            tf_titre_a.setText(selectedAnnonce.getTitreA());
            tf_description_a.setText(selectedAnnonce.getDescriptionA());
            tf_ville_a.setValue(selectedAnnonce.getVilleA());
            tf_date_debut_a.setValue(selectedAnnonce.getDateDebut());
            tf_date_fin_a.setText(selectedAnnonce.getDateFin());
            tf_nb_jour_a.setText(String.valueOf(selectedAnnonce.getNbJour()));
            tf_user_id.setText(selectedAnnonce.getUser());
            tf_maps_link.setText(selectedAnnonce.getMapsLink());
        }
    }


//    private ObservableList<Annonce> getAnnonceList() throws SQLException {
//        ObservableList<Annonce> annonces = FXCollections.observableArrayList();
//        annonces.addAll(AnnonceService.afficher());
//        return annonces;
//    }

    @FXML
    private void loadAnnonceData() throws SQLException {
        ObservableList<Annonce> annonces = getAnnonceList();
        tvAnnonces.setItems(annonces);
        int pageCount = calculatePageCount();
        pagination.setPageCount(pageCount);
        pagination.setCurrentPageIndex(0);
    }

    @FXML
    private void onTableClick(MouseEvent event) {
        if (event.getClickCount() == 1) {
            Annonce selectedAnnonce = tvAnnonces.getSelectionModel().getSelectedItem();
            if (selectedAnnonce != null) {
                displayMap(selectedAnnonce.getMapsLink());
            }
        }
    }

    private void displayMap(String mapsLink) {
        // Logic to display map based on mapsLink
        // Example: create a MapView and add it to the VBox containing the maps
        MapView mapView = createMapView(mapsLink);
        adresse.getChildren().clear(); // Clear existing maps
        adresse.getChildren().add(mapView);
        VBox.setVgrow(mapView, Priority.ALWAYS);
    }


    @Deprecated
    void ajouterAnnonce(ActionEvent event) throws SQLException {
        String titreValue = tf_titre_a.getText();
        String descriptionValue = tf_description_a.getText();
        String villeValue = tf_ville_a.getValue();
        LocalDate dateDebutValue = tf_date_debut_a.getValue();
        String dateFinValue = tf_date_fin_a.getText();
        String nbJourValue = tf_nb_jour_a.getText();
        String userIdValue = tf_user_id.getText();
        String mapsLinkValue = tf_maps_link.getText();

        // Vérification des valeurs saisies
        if (titreValue.isEmpty() || descriptionValue.isEmpty() || villeValue.isEmpty() || dateDebutValue == null || dateFinValue == null || nbJourValue.isEmpty() || userIdValue.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Ajout impossible", "Veuillez remplir tous les champs.");
            return;
        }

        if (titreValue.length() > 100) {
            showAlert(Alert.AlertType.WARNING, "Ajout impossible", "Le champ titre ne doit pas dépasser 100 caractères.");
            return;
        }

        if (!isValidDate(dateDebutValue) ) {
            showAlert(Alert.AlertType.WARNING, "Dates incorrectes", "Les dates doivent être au format jj/mm/aaaa et la date de début doit être antérieure à la date de fin.");
            return;
        }

        if (!isValidNbJour(nbJourValue)) {
            showAlert(Alert.AlertType.WARNING, "Nombre de jours incorrect", "Le champ nombre de jours doit contenir des chiffres uniquement.");
            return;
        }

        // Fetch the User object based on the provided user ID
        User user = AnnonceService.getUserById(userIdValue);

        if (user == null) {
            showAlert(Alert.AlertType.WARNING, "Utilisateur non trouvé", "Aucun utilisateur trouvé avec cet ID.");
            return;
        }

        Annonce annonce = new Annonce(titreValue, descriptionValue, villeValue, dateDebutValue, nbJourValue, dateFinValue, userIdValue, mapsLinkValue);

        try {
            AnnonceService.ajouter(annonce);
            showAlert(Alert.AlertType.CONFIRMATION, "Succès", "Annonce ajoutée avec succès.");
            loadAnnonceData();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout de l'annonce : " + e.getMessage());
        }
    }

    @Deprecated
    void modifierAnnonce(ActionEvent event) {
        Annonce annonce = tvAnnonces.getSelectionModel().getSelectedItem();
        if (annonce != null) {
            String titre = tf_titre_a.getText();
            String description = tf_description_a.getText();
            String ville = tf_ville_a.getValue();

            LocalDate dateDebut = tf_date_debut_a.getValue();
            String nbJour = tf_nb_jour_a.getText();
            String dateFin = tf_date_fin_a.getText();
            String user = tf_user_id.getText();
            String maps_link = tf_maps_link.getText();

            annonce.setTitreA(titre);
            annonce.setDescriptionA(description);
            annonce.setVilleA(ville);
            annonce.setDateDebut(dateDebut);
            annonce.setDateFin(LocalDate.parse(dateFin));
            annonce.setNbJour(nbJour);
            annonce.setUser(user);
            annonce.setMapsLink(maps_link);

            try {
                AnnonceService.modifier(annonce);
                loadAnnonceData();

                showAlert(Alert.AlertType.CONFIRMATION, "Succès", "Annonce modifiée avec succès");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucune annonce sélectionnée", "Veuillez sélectionner une annonce à modifier.");
        }
    }
    private void supprimerAnnonce() {
        Annonce annonce = tvAnnonces.getSelectionModel().getSelectedItem();
        if (annonce != null) {
            try {
                AnnonceService.supprimer(annonce);
                loadAnnonceData();
                showAlert(Alert.AlertType.INFORMATION, "Suppression réussie", "L'annonce a été supprimée avec succès.");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur lors de la suppression", "Une erreur s'est produite lors de la suppression de l'annonce : " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucune annonce sélectionnée", "Veuillez sélectionner une annonce.");
        }
    }

    private boolean isValidDate(LocalDate date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY/MM/DD");
            formatter.format(date);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean isValidNbJour(String nbJour) {
        return nbJour.matches("\\d+");
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @Deprecated
    private void searchAnnonce(String searchString) throws SQLException {
        if (searchString.isEmpty()) {
            // If search text is empty, reload all annonces
            loadAnnonceData();
        } else {
            List<Annonce> filteredAnnonce = AnnonceService.afficher().stream()
                    .filter(annonce -> annonce.getTitreA().toLowerCase().startsWith(searchString.toLowerCase()))
                    .collect(Collectors.toList());

            ObservableList<Annonce> observableFilteredAnnonce = FXCollections.observableArrayList(filteredAnnonce);
            tvAnnonces.setItems(observableFilteredAnnonce);
        }
    }
//    private void generateQRCode(String userId) {
//        try {
//            // Fetch user information based on userId
//            User user = AnnonceService.getUserById(userId);
//
//            // Construct user information string
//            String userInfo = "User ID: " + user.getId() + "\n" +
//                    "Email: " + user.getEmail() + "\n"
//                    // Add other user information as needed...
//                    ;
//
//            // Generate QR code from user information
//            ByteArrayOutputStream out = QRCode.from(userInfo).stream();
//
//            // Convert QR code to JavaFX Image
//            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
//            Image image = new Image(in);
//
//            // Display QR code in an ImageView
//            ImageView imageView = new ImageView(image);
//
//            // Create a StackPane to hold the ImageView
//            StackPane pane = new StackPane(imageView);
//
//            // Show QR code in a dialog
//            Dialog<Void> dialog = new Dialog<>();
//            dialog.getDialogPane().setContent(pane);
//            dialog.initStyle(StageStyle.UTILITY);
//            dialog.showAndWait();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
@FXML
private void exportToExcel(ActionEvent event) {
    // Get the data from the TableView
    ObservableList<Annonce> data = tvAnnonces.getItems();

    // Create a new workbook
    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("Annonce Data");

    // Create header row
    Row headerRow = sheet.createRow(0);
    headerRow.createCell(0).setCellValue("Id");
    headerRow.createCell(1).setCellValue("Titre");
    headerRow.createCell(2).setCellValue("Description");
    headerRow.createCell(3).setCellValue("Ville");
    headerRow.createCell(4).setCellValue("Date debut");
    headerRow.createCell(5).setCellValue("NB jour");
    headerRow.createCell(6).setCellValue("Date fin");
    headerRow.createCell(7).setCellValue("User id");
    headerRow.createCell(8).setCellValue("Lien maps");


    // Populate data rows
    int rowNum = 1;
    for (Annonce annonce : data) {
        Row row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue(annonce.getIdAnnonce());
        row.createCell(1).setCellValue(annonce.getTitreA()); // Assuming 'Annee' is of type LocalDate
        row.createCell(2).setCellValue(annonce.getDescriptionA());
        row.createCell(3).setCellValue(annonce.getVilleA());
        row.createCell(4).setCellValue(annonce.getDateDebut());
        row.createCell(5).setCellValue(annonce.getNbJour());
        row.createCell(6).setCellValue(annonce.getDateFin());
        row.createCell(7).setCellValue(annonce.getUser());
        row.createCell(8).setCellValue(annonce.getMapsLink());
    }

    // Choose file location
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save Excel File");
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "C:/Users/alima/Downloads/Annonces.xlsx"));
    File file = fileChooser.showSaveDialog(null);
    if (file != null) {
        // Write the workbook content to the chosen file
        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            workbook.write(fileOut);
            System.out.println("Excel file exported successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Open the exported file
        try {
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
    @FXML
    private void showStatistics() {
        ObservableList<Annonce> annonces = tvAnnonces.getItems();
        if (!annonces.isEmpty()) {
            // Group annonces by villeA
            Map<String, List<Annonce>> annoncesByVille = annonces.stream()
                    .collect(Collectors.groupingBy(Annonce::getVilleA));

            // Calculate statistics for each group
            Map<String, Long> countByVille = new HashMap<>();
            annoncesByVille.forEach((ville, list) -> {
                long count = list.size(); // Count the number of annonces in each group
                countByVille.put(ville, count);
            });

            // Create bar chart
            // Create bar chart
            CategoryAxis xAxis = new CategoryAxis();
            NumberAxis yAxis = new NumberAxis();
            BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
            barChart.setTitle("Statistics");

// Populate bar chart with data
            countByVille.forEach((ville, count) -> {
                XYChart.Series<String, Number> series = new XYChart.Series<>();
                series.setName(ville);
                series.getData().add(new XYChart.Data<>( ville, count)); // Use 'ville' as label
                barChart.getData().add(series);
            });

// Adjust width of bars
            double barWidth = annoncesByVille.size() > 1 ? 20 : 40;
            barChart.setCategoryGap(barWidth * 0.1);
            barChart.setBarGap(barWidth * 0.2);

// Set the width of category axis labels
            xAxis.setTickLabelRotation(90); // Rotate labels for better readability
            xAxis.setTickLabelFont(Font.font(10)); // Adjust font size if needed
// Create and show an alert dialog containing the bar chart
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setTitle("Statistics");
            alert.setHeaderText(null);
            alert.getDialogPane().setContent(barChart);
            alert.getButtonTypes().addAll(ButtonType.OK);

// Adjust dialog size
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.setMinHeight(600);
            dialogPane.setMinWidth(200);

            Optional<ButtonType> result = alert.showAndWait();

        } else {
            // No data to calculate statistics
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Data");
            alert.setHeaderText(null);
            alert.setContentText("No annonce data available.");
            alert.showAndWait();
        }
    }



    @FXML
    void Confirmer(ActionEvent event) {
        try {
            // Prompt the user to perform an action
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Human Verification");
            alert.setHeaderText(null);
            alert.setContentText("Please click on the button below to confirm that you're human.");
            ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
            alert.getButtonTypes().setAll(confirmButtonType, ButtonType.CANCEL);
            Optional<ButtonType> result = alert.showAndWait();

            // Verify user action
            if (result.isPresent() && result.get() == confirmButtonType) {
                // Schedule the capture of image after 4 seconds
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(4), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Platform.runLater(() -> {
                            try {
                                // Capture an image from the webcam
                                Webcam webcam = discoverWebcam();
                                if (webcam != null) {
                                    webcam.open();
                                    BufferedImage bufferedImage = webcam.getImage();
                                    webcam.close();

                                    // Convert BufferedImage to JavaFX Image
                                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                                    try {
                                        ImageIO.write(bufferedImage, "png", outputStream);
                                        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                                        Image image = new Image(inputStream);
                                        webcamView.setImage(image);

                                        // Detection de visage avec OpenCV
                                        CascadeClassifier faceDetector = new CascadeClassifier("haarcascade_frontalface_default.xml");
                                        Mat mat = OpenCVUtils.bufferedImageToMat(bufferedImage);
                                        MatOfRect faceDetections = new MatOfRect();
                                        faceDetector.detectMultiScale(mat, faceDetections);

                                        // Si un visage est détecté, procédez à la confirmation
                                        if (!faceDetections.empty()) {
                                            // Procéder à la confirmation des sièges
//                                            int id = 0;
//                                            String siddd = Sid.getText();
//                                            seeats seat = new seeats(id, siddd);
//                                            serviceseat sr = new serviceseat();
//                                            sr.insertOne(seat);
//                                            String total = Sid.getText();
//                                            selectedSeats.setText(total);

                                            // Afficher le message de confirmation
                                            Alert confirmationAlert = new Alert(Alert.AlertType.INFORMATION);
                                            confirmationAlert.setTitle("Confirmation");
                                            confirmationAlert.setHeaderText(null);
                                            confirmationAlert.setContentText("Confirmation faite!");
                                            confirmationAlert.showAndWait();
                                        } else {
                                            // Aucun visage n'a été détecté, afficher un message d'erreur
                                            showAlert("Error", "Aucun visage n'a été détecté dans l'image.");
                                        }
                                    } catch (IOException e) {
                                        showAlert("Error", "Failed to convert image.");
                                    }
                                } else {
                                    showAlert("Error", "No webcam available.");
                                }
                            } catch (AWTException e) {
                                showAlert("Error", "An error occurred while confirming: " + e.getMessage());
                            }
                        });
                    }
                }));
                timeline.setCycleCount(1);
                timeline.play();
            } else {
                // User did not confirm, display an error message
                showAlert("Error", "Confirmation not done. Please confirm that you're human.");
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "An error occurred while confirming: " + e.getMessage());
        }
    }

    private Webcam discoverWebcam() throws AWTException {
        Webcam webcam = Webcam.getDefault(); // Recherche de la webcam par défaut
        if (webcam == null) {
            throw new AWTException("No webcam available.");
        }
        return webcam;}



    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
//    @FXML
//    private void translate(ActionEvent event) {
//        String inputText = inputTextArea.getText();
//        String translatedText = translator.translate(inputText, Language.ENGLISH, Language.FRENCH);
//        outputTextArea.setText(translatedText);
//    }


    @FXML
    private void loadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.hdr", "*.jpg", "*.png", "*.gif"));

        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            try {
                BufferedImage bufferedImage = hdrToBufferedImage(selectedFile);
                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                imageView.setImage(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private BufferedImage hdrToBufferedImage(File hdrFile) throws IOException {
        BufferedImage bi = null;
        ImageInputStream input = ImageIO.createImageInputStream(hdrFile);
        try {
            Iterator<ImageReader> readers = ImageIO.getImageReaders(input);
            if (!readers.hasNext()) {
                throw new IllegalArgumentException("No reader for: " + hdrFile);
            }
            ImageReader reader = readers.next();
            try {
                reader.setInput(input);

                // Read the image with the specified settings
                bi = reader.read(0, reader.getDefaultReadParam());

                // You may want to perform further processing on the image here
            } finally {
                reader.dispose();
            }
        } finally {
            input.close();
        }
        return bi;
    }
    public void loadImage(String imagePath) {
        Image image = new Image(getClass().getResourceAsStream(imagePath));
        imageView.setImage(image);
    }
    private int calculatePageCount() {
        int pageCount = (int) Math.ceil(tvAnnonces.getItems().size() / (double) itemsPerPage);
        return pageCount;
    }

    private void updateTableView(int pageIndex) throws SQLException {
        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, getAnnonceList().size());
        ObservableList<Annonce> pageData = FXCollections.observableArrayList(getAnnonceList().subList(fromIndex, toIndex));
        tvAnnonces.setItems(pageData);
    }
    private ObservableList<Annonce> getAnnonceList() throws SQLException {
        ObservableList<Annonce> annonces = FXCollections.observableArrayList();
        annonces.addAll(AnnonceService.afficher());
        return annonces;
    }








}

