package controllers;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;

import javafx.event.ActionEvent;
//import java.awt.event.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Voiture;
import services.VoitureService;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.control.Alert;

import javafx.scene.control.TableView;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javafx.stage.FileChooser;
import net.glxn.qrgen.javase.QRCode;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import net.glxn.qrgen.core.image.ImageType;
import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;


public class AfficherVControllers {

    @FXML
    private TextField txtVoiture_Marque;

    @FXML
    private DatePicker txtAnnee;

    @FXML
    private TextField txtPrix_j;


    @FXML
    private TextField txtId;

    @FXML
    private TextField txtKilometrage;

    @FXML
    private TextField txtNbrPlaces;

    @FXML
    private TableView<Voiture> tableviewVoiture;

    @FXML
    private TableColumn<Voiture, String> colMarque;

    @FXML
    private TableColumn<Voiture, LocalDate> colAnnee;

    @FXML
    private TableColumn<Voiture, Integer> colPrix_j;

    @FXML
    private TableColumn<Voiture, Integer> colKilometrage;

    @FXML
    private TableColumn<Voiture, Integer> colNbrPlaces;

    private VoitureService voitureService;
    private ObservableList<Voiture> voitureList;

    @FXML
    private Button deleteButton;

    @FXML
    private Button updateButton;

    @FXML
    private TextField searchTextField;

    @FXML
    private Button searchButton;

    @FXML
    private ImageView qrCodeImageView;



    private Stage stage;
    private Scene scene;
    private Parent root;





    public void setTxtVoiture_Marque(String txtVoiture_Marque) {
        this.txtVoiture_Marque.setText(txtVoiture_Marque);
    }

    public void setTxtAnnee(LocalDate txtAnnee) {
        this.txtAnnee.setValue(txtAnnee);
    }

    public void setTxtPrix_j(String txtPrix_j) {
        this.txtPrix_j.setText(txtPrix_j);
    }

    public void setTxtKilometrage(String txtKilometrage) {
        this.txtKilometrage.setText(txtKilometrage);
    }

    public void setTxtNbrPlaces(String txtNbrPlaces) {
        this.txtNbrPlaces.setText(txtNbrPlaces);
    }

    public void setTxtId(String txtId) {
        this.txtId.setText(txtId);
    }



    public void goToModification(javafx.event.ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierV.fxml"));

        try {
            Parent root = loader.load();
            ModifierVoitureController modifierController = loader.getController();
            modifierController.setTxtAnnee(txtAnnee.getValue());
            modifierController.setTxtVoiture_Marque(txtVoiture_Marque.getText());
            modifierController.setTxtPrix_j(txtPrix_j.getText());
            modifierController.setTxtKilometrage(txtKilometrage.getText());
            modifierController.setTxtNbrPlaceset(txtNbrPlaces.getText());
            modifierController.setTxtId(txtId.getText());

            // Debug statement to check if root is loaded successfully
            System.out.println("FXML loaded successfully.");

            txtAnnee.getScene().setRoot(root);
        } catch (IOException e) {
            // Instead of just printing, handle the IOException
            e.printStackTrace();
        }
    }

    private void selectVoiture(MouseEvent event) {
        if (event.getClickCount() == 1) { // Vérifie si un clic simple a été effectué
            Voiture selectedVoiture = tableviewVoiture.getSelectionModel().getSelectedItem();
            if (selectedVoiture != null) {
                // Affichage des détails du voyage sélectionné dans le formulaire
                txtVoiture_Marque.setText(selectedVoiture.getMarque());
                txtAnnee.setValue(selectedVoiture.getAnnee());
                txtPrix_j.setText(String.valueOf(selectedVoiture.getPrix_j()));
                txtKilometrage.setText(String.valueOf(selectedVoiture.getKilometrage()));
                txtNbrPlaces.setText(String.valueOf(selectedVoiture.getNbrPlaces()));
            }
        }
    }

    public void initialize() {
        voitureService = new VoitureService();
        setupTableView();
        loadVoitures();
        String marque = txtVoiture_Marque.getText();
        loadUserAndGenerateQR(marque);
    }

    private void setupTableView() {
        colMarque.setCellValueFactory(new PropertyValueFactory<>("marque"));
        colAnnee.setCellValueFactory(new PropertyValueFactory<>("annee"));
        colPrix_j.setCellValueFactory(new PropertyValueFactory<>("prix_j"));
        colKilometrage.setCellValueFactory(new PropertyValueFactory<>("kilometrage"));
        colNbrPlaces.setCellValueFactory(new PropertyValueFactory<>("nbrPlaces"));
    }

    private void loadVoitures() {
        try {
            List<Voiture> voitures = voitureService.recuperer();
            tableviewVoiture.getItems().addAll(voitures);
        } catch (SQLException e) {
            e.printStackTrace(); // Gérer l'erreur
        }
    }


    @FXML
    public void handleRowSelection(MouseEvent event) {
        Voiture selectedVoiture = tableviewVoiture.getSelectionModel().getSelectedItem();
        if (selectedVoiture != null) {
            deleteButton.setDisable(false); // Enable delete button
            updateButton.setDisable(false); // Enable update button
        } else {
            deleteButton.setDisable(true); // Disable delete button if no row is selected
            updateButton.setDisable(true); // Disable update button if no row is selected
        }
    }

    @FXML
    public void handleDeleteButton(ActionEvent event) {
        Voiture selectedVoiture = tableviewVoiture.getSelectionModel().getSelectedItem();
        if (selectedVoiture != null) {
            try {
                // Call your delete method with selectedVoiture.getId() as parameter
                voitureService.supprimer(selectedVoiture.getId_voiture());
                // Remove the selected row from the TableView
                tableviewVoiture.getItems().remove(selectedVoiture);
            } catch (SQLException e) {
                e.printStackTrace(); // Handle error
            }
        }
    }

    public void handleUpdateButton(ActionEvent event) {
        Voiture selectedVoiture = tableviewVoiture.getSelectionModel().getSelectedItem();
        if (selectedVoiture != null) {
            try {
                // Load the ModifierVoiture.fxml file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierV.fxml"));
                Parent root = loader.load();

                // Get the controller for ModifierV.fxml
                ModifierVoitureController modifierController = loader.getController();

                // Pass the selected voiture to the controller to pre-fill fields
                modifierController.setTextFields(selectedVoiture);

                // Create a new stage
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Modifier Voiture");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace(); // Handle error loading FXML
            }

        }
    }

    @FXML
    private void searchVoitures() {
        String searchString = searchTextField.getText().toLowerCase();

        tableviewVoiture.getItems().clear(); // Clear previous items

        try {
            if (searchString.isEmpty()) {
                // If search text is empty, reload all voitures
                loadVoitures();
            } else {
                List<Voiture> filteredVoitures = voitureService.recuperer().stream()
                        .filter(voiture -> voiture.getMarque().toLowerCase().contains(searchString))
                        .collect(Collectors.toList());

                tableviewVoiture.getItems().addAll(filteredVoitures);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the SQLException
            // Optionally, display an error message to the user
        }
    }

    @FXML
    private void handleSortButtonClick() {
        // Call a method to sort the table
        sortTable();
    }

    private void sortTable() {
        // Perform sorting logic here
        // For example, to sort by the Marque column in ascending order
        colMarque.setSortType(TableColumn.SortType.ASCENDING);
        tableviewVoiture.getSortOrder().clear();
        tableviewVoiture.getSortOrder().add(colMarque);
        tableviewVoiture.sort();
    }

    @FXML
    private void showStatistics() {
        ObservableList<Voiture> voitures = tableviewVoiture.getItems();
        if (!voitures.isEmpty()) {
            // Calculate statistics
            DoubleSummaryStatistics stats = voitures.stream()
                    .mapToDouble(Voiture::getPrix_j)
                    .summaryStatistics();

            // Display statistics in a bar chart
            CategoryAxis xAxis = new CategoryAxis();
            NumberAxis yAxis = new NumberAxis();
            BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.getData().add(new XYChart.Data<>("Min", stats.getMin()));
            series.getData().add(new XYChart.Data<>("Max", stats.getMax()));
            series.getData().add(new XYChart.Data<>("Average", stats.getAverage()));
            barChart.getData().add(series);

            // Create and show an alert dialog containing the bar chart
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Statistics");
            alert.setHeaderText(null);
            alert.getDialogPane().setContent(barChart);
            alert.showAndWait();
        } else {
            // No data to calculate statistics
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Data");
            alert.setHeaderText(null);
            alert.setContentText("No voiture data available.");
            alert.showAndWait();
        }
    }

    @FXML
    private void exportToExcel(ActionEvent event) {
        // Get the data from the TableView
        ObservableList<Voiture> data = tableviewVoiture.getItems();

        // Create a new workbook
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Voiture Data");

        // Create header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Marque");
        headerRow.createCell(1).setCellValue("Annee");
        headerRow.createCell(2).setCellValue("Prix par jour");
        headerRow.createCell(3).setCellValue("Kilométrage");
        headerRow.createCell(4).setCellValue("Nombre de Places");

        // Populate data rows
        int rowNum = 1;
        for (Voiture voiture : data) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(voiture.getMarque());
            row.createCell(1).setCellValue(voiture.getAnnee().toString()); // Assuming 'Annee' is of type LocalDate
            row.createCell(2).setCellValue(voiture.getPrix_j());
            row.createCell(3).setCellValue(voiture.getKilometrage());
            row.createCell(4).setCellValue(voiture.getNbrPlaces());
        }

        // Choose file location
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Excel File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "C:/Users/user/Desktop/workshopPidev2324-sprintJava/voitures.xlsx"));
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

    @FXML
    private void generateQRCode(ActionEvent event) {
        // Get the selected car from the tableview
        Voiture selectedVoiture = tableviewVoiture.getSelectionModel().getSelectedItem();
        if (selectedVoiture != null) {
            // Construct the message for the QR code using the car's information
            String message = String.format("Marque: %s\nAnnée: %s\nPrix par jour: %s\nKilométrage: %s\nNombre de places: %s",
                    selectedVoiture.getMarque(), selectedVoiture.getAnnee(), selectedVoiture.getPrix_j(),
                    selectedVoiture.getKilometrage(), selectedVoiture.getNbrPlaces());

            try {
                // Generate the QR code image
                BufferedImage qrCodeImage = generateQRCode(message);

                // Convert the BufferedImage to JavaFX Image
                Image qrCodeFXImage = SwingFXUtils.toFXImage(qrCodeImage, null);

                // Set the generated QR code image to the qrCodeImageView
                qrCodeImageView.setImage(qrCodeFXImage);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        } else {
            // No car is selected, display an error message or handle it as needed
            System.out.println("No car selected.");
        }
    }

    private void loadUserAndGenerateQR(String marque) {
        // Database connection parameters
        String url = "jdbc:mysql://localhost:3306/tunvista";
        String username = "root";
        String password = "";

        // SQL query to fetch user details based on user ID
        String query = "SELECT marque, annee, prix_j, kilometrage, nbrPlaces FROM voiture WHERE marque = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Set the user ID parameter in the prepared statement
            statement.setString(1, marque);

            // Execute the query
            try (ResultSet resultSet = statement.executeQuery()) {
                // Check if user with the given ID exists
                /*if (resultSet.next()) {
                    // Retrieve user details from the result set
                    String Marque = resultSet.getString("marque");
                    String annee = resultSet.getString("annee");
                    String prix_j = resultSet.getString("prix_j");
                    String kilometrage = resultSet.getString("kilometrage");
                    String nbrPlaces = resultSet.getString("nbrPlaces");

                    // Format user information as a string
                    String userInfo = String.format("marque: %s\nannee: %s\nprix_j: %s\nkilometrage: %s\nnbrPlaces: %s",
                            Marque, annee, prix_j, kilometrage, nbrPlaces);

                    // Generate QR code
                    //generateQRCode(userInfo);
                } else {
                    // User with the given ID does not exist
                    //System.out.println("Voiture not found");
                }*/
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database connection or query errors
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



}
