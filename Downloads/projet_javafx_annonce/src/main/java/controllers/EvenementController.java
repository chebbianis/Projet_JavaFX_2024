package controllers;

import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.stage.StageStyle;
import models.Evenement;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import services.EvenementService;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.RED;


public class EvenementController {
    @FXML
    private Button btn_ajouter;
    @FXML
    private Button btn_modifier;
    @FXML
    private Button btn_supprimer;
    @FXML
    private Button btn_search;
    @FXML
    private TextField tf_titre_e;
    @FXML
    private TextField tf_description_e;
    @FXML
    private ComboBox<String> tf_ville_e;
    @FXML
    private DatePicker tf_date_debut_e;
    @FXML
    private TextField tf_date_fin_e;
    @FXML
    private TextField tf_nb_jour_e;
    @FXML
    private TableView<Evenement> tvEvenements;
    @FXML
    private TableColumn<Evenement, String> colIdEvenement;
    @FXML
    private TableColumn<Evenement, String> colTitreEvenement;
    @FXML
    private TableColumn<Evenement, String> colDescriptionEvenement;
    @FXML
    private TableColumn<Evenement, String> colVilleEvenement;
    @FXML
    private TableColumn<Evenement, LocalDate> colDateDebutE;
    @FXML
    private TableColumn<Evenement, LocalDate> colDateFinE;
    @FXML
    private TableColumn<Evenement, String> colNbJourE;
    @FXML
    private TextField tf_search;
    @FXML
    private VBox adresse_b;
    private final Map<String, String> cityCoordinates_b = new HashMap<>();

    private final ObservableList<String> cityList_b = FXCollections.observableArrayList();

    @FXML
    private Button statistiqueButton_b;
    @FXML
    private Button exportButton_b;
    @FXML
    private Button bn_pdf;
    @FXML
    private MapView mapView_b;

    @FXML
    private Pagination pagination_b;
    private int itemsPerPage = 14;
    @FXML
    private Button exportPdfButton;


    private final EvenementService EvenementService = new EvenementService();

    public void initialize() throws SQLException {
        // Initialisation des colonnes et chargement des données
        colIdEvenement.setCellValueFactory(new PropertyValueFactory<>("idEvenement"));
        colTitreEvenement.setCellValueFactory(new PropertyValueFactory<>("titreE"));
        colDescriptionEvenement.setCellValueFactory(new PropertyValueFactory<>("descriptionE"));
        colVilleEvenement.setCellValueFactory(new PropertyValueFactory<>("villeE"));
        colDateDebutE.setCellValueFactory(new PropertyValueFactory<>("dateDebutE"));
        colNbJourE.setCellValueFactory(new PropertyValueFactory<>("nbJourE"));
        colDateFinE.setCellValueFactory(new PropertyValueFactory<>("dateFinE"));



        tf_date_debut_e.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateDateFin(newValue, tf_nb_jour_e.getText());
        });
        tf_nb_jour_e.textProperty().addListener((observable, oldValue, newValue) -> {
            updateDateFin(tf_date_debut_e.getValue(), newValue);
        });

        tf_search.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                searchEvenement(newValue);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        //tf_search.setTextFormatter(textFormatter);

        loadEvenementData(); // Chargement des données dans la TableView

        // Ajout de l'événement pour sélectionner une evneement dans le formulaire
        tvEvenements.setOnMouseClicked(event -> selectEvenement(event));

        btn_supprimer.setOnAction(event -> supprimerEvenement());
        btn_modifier.setOnAction(event -> modifierEvenement(event));
        btn_ajouter.setOnAction(event ->  {
            ajouterEvenement(event);

        });
        tvEvenements.setOnMouseClicked(event -> {
            Evenement selectedEvenement = tvEvenements.getSelectionModel().getSelectedItem();
            if (selectedEvenement != null) {
                handleEvenementtClick(selectedEvenement);
            }
        });
        mapView_b = createMapView();
        adresse_b.getChildren().add(mapView_b);
        VBox.setVgrow(mapView_b, Priority.ALWAYS);
        cityList_b.addAll(
                "Ariana", "Béja", "Ben Arous", "Bizerte", "Gabès", "Gafsa",
                "Jendouba", "Kairouan", "Kasserine", "Kébili", "Le Kef", "Mahdia",
                "Manouba", "Medenine", "Monastir", "Nabeul", "Sfax", "Sidi Bouzid",
                "Siliana", "Sousse", "Tataouine", "Tozeur", "Tunis", "Zaghouan"
        );
        tf_ville_e.setItems(cityList_b);
        int totalItems = getEvenementList().size();
        pagination_b = new Pagination();
        pagination_b.setPageCount(calculatePageCount());
        pagination_b.setCurrentPageIndex(0);
        adresse_b.getChildren().add(pagination_b);
        int pageCount = (int) Math.ceil((double) totalItems / itemsPerPage);
        pagination_b.setPageCount(pageCount);
        pagination_b.setCurrentPageIndex(0);
        updateTableView(0); // Display the first page initially

        pagination_b.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            try {
                updateTableView((int) newIndex);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        bn_pdf.setOnAction(event -> {
            List<Evenement> evenements = null;
            try {
                evenements = getEvenementList();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            String fileName = "C:\\Users\\alima\\Downloads\\Evenement.pdf";
            generatePDF(evenements, fileName);
        });

    }



    private void updateDateFin(LocalDate dateDebutE, String nbJourE) {
        if (dateDebutE != null && !nbJourE.isEmpty()) {
            try {
                int nbJourInt = Integer.parseInt(nbJourE);
                String dateFinE = String.valueOf(dateDebutE.plusDays(nbJourInt));
                tf_date_fin_e.setText(dateFinE);
            } catch (NumberFormatException e) {
                // Handle invalid nbJour input
                tf_date_fin_e.setText(null);
            }
        } else {
            tf_date_fin_e.setText(null);
        }
    }
    private void initializeCityCoordinates() {
        cityCoordinates_b.put("Tunis", "36.806745,10.181392");
        cityCoordinates_b.put("Ariana", "36.866609,10.164723");
        cityCoordinates_b.put("Ben Arous", "36.743565,10.231954");
        cityCoordinates_b.put("La Manouba", "36.809316,10.086341");
        cityCoordinates_b.put("Nabeul", "36.451232,10.735811");
        cityCoordinates_b.put("Zaghouan", "36.409067,10.142286");
        cityCoordinates_b.put("Bizerte", "37.276630,9.864115");
        cityCoordinates_b.put("Béja", "36.733278,9.184363");
        cityCoordinates_b.put("Jendouba", "36.507148,8.775573");
        cityCoordinates_b.put("Le Kef", "36.167908,8.709562");
        cityCoordinates_b.put("Siliana", "36.088118,9.364283");
        cityCoordinates_b.put("Kairouan", "35.671018,10.100636");
        cityCoordinates_b.put("Kasserine", "35.171903,8.830873");
        cityCoordinates_b.put("Sidi Bouzid", "35.035419,9.483931");
        cityCoordinates_b.put("Sousse", "35.823978,10.634162");
        cityCoordinates_b.put("Monastir", "35.764156,10.811427");
        cityCoordinates_b.put("Mahdia", "35.502247,11.045700");
        cityCoordinates_b.put("Sfax", "34.739398,10.760217");
        cityCoordinates_b.put("Gabès", "33.887756,10.097669");
        cityCoordinates_b.put("Médenine", "33.339856,10.495841");
        cityCoordinates_b.put("Tataouine", "32.920859,10.450973");
        cityCoordinates_b.put("Kébili", "33.706912,8.971528");
        cityCoordinates_b.put("Gafsa", "34.430931,8.775695");
        cityCoordinates_b.put("Tozeur", "33.918158,8.123013");
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
        EvenementController.CustomMapLayer customMapLayer = new EvenementController.CustomMapLayer();
        customMapLayer.layoutLayer(latitude, longitude);

        // Add the custom map layer to the map
        mapView.addLayer(customMapLayer);

        return mapView;
    }
    private final MapPoint eiffelPoint = new MapPoint(36.806745,10.181392);

    private MapView createMapView(){
        MapView mapView = new MapView();
        mapView.setPrefSize(500, 400);
        mapView.addLayer(new CustomMapLayer());
        mapView.setZoom(15);
        mapView.flyTo(0,eiffelPoint,0.1);
        return mapView;
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
            Circle marker = new Circle(10, RED);
            marker.setStroke(BLACK);
            marker.setStrokeWidth(2);
            return marker;
        }
    }
    @Deprecated
    private void handleEvenementtClick(Evenement evenement) {
        String city = evenement.getVilleE();
        String coordinates = getCoordinatesByCity(city);

        if (evenement != null) {
            tf_titre_e.setText(evenement.getTitreE());
            tf_description_e.setText(evenement.getDescriptionE());
            tf_ville_e.setValue(evenement.getVilleE());
            tf_date_debut_e.setValue(evenement.getDateDebutE());
            tf_date_fin_e.setText(evenement.getDateFinE());
            tf_nb_jour_e.setText(evenement.getNbJourE());
        }
        if (coordinates != null) {
            String[] parts = coordinates.split(",");
            double latitude = Double.parseDouble(parts[0]);
            double longitude = Double.parseDouble(parts[1]);
            mapView_b.setCenter(latitude, longitude);
        } else {
            showAlert(Alert.AlertType.ERROR, "Coordonnées introuvables",
                    "Les coordonnées de la ville sélectionnée sont introuvables.");
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


    private void selectEvenement(MouseEvent event) {
        if (event.getClickCount() == 1) { // Vérifie si un clic simple a été effectué
            Evenement selectedEvenement = tvEvenements.getSelectionModel().getSelectedItem();
            if (selectedEvenement != null) {
                // Affichage des détails de l'evenement sélectionnée dans le formulaire
                tf_titre_e.setText(selectedEvenement.getTitreE());
                tf_description_e.setText(selectedEvenement.getDescriptionE());
                tf_ville_e.setValue(selectedEvenement.getVilleE());
                tf_date_debut_e.setValue(selectedEvenement.getDateDebutE());
                tf_date_fin_e.setText(selectedEvenement.getDateFinE());
                tf_nb_jour_e.setText(String.valueOf(selectedEvenement.getNbJourE()));
            }
        }
    }
    @FXML
    private void loadEvenementData() throws SQLException {
        ObservableList<Evenement> evenements = getEvenementList();
        tvEvenements.setItems(evenements);
        int pageCount = calculatePageCount();
        pagination_b.setPageCount(pageCount);
        pagination_b.setCurrentPageIndex(0);
    }

    private void updateMapView(String mapsLink) {
        MapView mapView = createMapView(mapsLink);
        adresse_b.getChildren().clear(); // Clear existing map
        adresse_b.getChildren().add(mapView);
        VBox.setVgrow(mapView, Priority.ALWAYS);
    }

    private ObservableList<Evenement> getEvenementList() throws SQLException {
        ObservableList<Evenement> evenements = FXCollections.observableArrayList();
        evenements.addAll(EvenementService.afficher());
        return evenements;
    }

//    private void loadEvenementData() throws SQLException {
//        ObservableList<Evenement> evenements = getEvenementList();
//        tvEvenements.setItems(evenements);
//    }

    @FXML
    void ajouterEvenement(ActionEvent event) {
        String titreValue = tf_titre_e.getText();
        String descriptionValue = tf_description_e.getText();
        String villeValue = tf_ville_e.getValue();
        LocalDate dateDebutValue = tf_date_debut_e.getValue();
        String dateFinValue = tf_date_fin_e.getText();
        String nbJourValue = tf_nb_jour_e.getText();

        // Vérification des valeurs saisies
        if (titreValue.isEmpty() || descriptionValue.isEmpty() || villeValue.isEmpty() || dateDebutValue == null || dateFinValue == null || nbJourValue.isEmpty()) {
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

        Evenement evenement = new Evenement(titreValue, descriptionValue, villeValue, dateDebutValue, nbJourValue, dateFinValue);

        try {
            EvenementService.ajouter(evenement);
            showAlert(Alert.AlertType.CONFIRMATION, "Succès", "Evenement ajoutée avec succès.");
            loadEvenementData();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout de l'evenement : " + e.getMessage());
        }
    }
    @FXML
    void modifierEvenement(ActionEvent event) {
        Evenement evenement = tvEvenements.getSelectionModel().getSelectedItem();
        if (evenement != null) {
            String titre = tf_titre_e.getText();
            String description = tf_description_e.getText();
            String ville = tf_ville_e.getValue();

            LocalDate dateDebut = tf_date_debut_e.getValue();
            String nbJour = tf_nb_jour_e.getText();
            String dateFin = tf_date_fin_e.getText();

            evenement.setTitreE(titre);
            evenement.setDescriptionE(description);
            evenement.setVilleE(ville);
            evenement.setDateDebutE(dateDebut);
            evenement.setDateFinE(LocalDate.parse(dateFin));
            evenement.setNbJourE(nbJour);

            try {
                EvenementService.modifier(evenement);
                loadEvenementData();

                showAlert(Alert.AlertType.CONFIRMATION, "Succès", "Evenement modifiée avec succès");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucune evenement sélectionnée", "Veuillez sélectionner une evenement à modifier.");
        }
    }
    private void supprimerEvenement() {
        Evenement evenement = tvEvenements.getSelectionModel().getSelectedItem();
        if (evenement != null) {
            try {
                EvenementService.supprimer(evenement);
                loadEvenementData();
                showAlert(Alert.AlertType.INFORMATION, "Suppression réussie", "L'evenement a été supprimée avec succès.");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur lors de la suppression", "Une erreur s'est produite lors de la suppression de l'evenement : " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucune evenement sélectionnée", "Veuillez sélectionner une evenement.");
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
    @FXML
    private void searchEvenement(String searchString) throws SQLException {
        if (searchString.isEmpty()) {
            // If search text is empty, reload all evenement
            loadEvenementData();
        } else {
            List<Evenement> filteredEvenement = EvenementService.afficher().stream()
                    .filter(evenement -> evenement.getTitreE().toLowerCase().startsWith(searchString.toLowerCase()))
                    .collect(Collectors.toList());

            ObservableList<Evenement> observableFilteredEvenement = FXCollections.observableArrayList(filteredEvenement);
            tvEvenements.setItems(observableFilteredEvenement);
        }
    }
    @FXML
    private void showStatistics() {
        ObservableList<Evenement> evenements = tvEvenements.getItems();
        if (!evenements.isEmpty()) {
            // Group evenements by villeA
            Map<String, List<Evenement>> evenementsByVille = evenements.stream()
                    .collect(Collectors.groupingBy(Evenement::getVilleE));

            // Calculate statistics for each group
            Map<String, Long> countByVille = new HashMap<>();
            evenementsByVille.forEach((ville, list) -> {
                long count = list.size(); // Count the number of evenements in each group
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
            double barWidth = evenementsByVille.size() > 1 ? 20 : 40;
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
            alert.setContentText("No evenement data available.");
            alert.showAndWait();
        }

    }
    private int calculatePageCount() {
        int pageCount = (int) Math.ceil(tvEvenements.getItems().size() / (double) itemsPerPage);
        return pageCount;
    }
    private void updateTableView(int pageIndex) throws SQLException {
        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, getEvenementList().size());
        ObservableList<Evenement> pageData = FXCollections.observableArrayList(getEvenementList().subList(fromIndex, toIndex));
        tvEvenements.setItems(pageData);
    }
    private static String truncateText(String text, float width) throws IOException {
        // If text width exceeds the specified width, truncate it
        if (PDType1Font.HELVETICA.getStringWidth(text) / 1000 * 12 > width) {
            // Calculate the maximum number of characters that can fit within the width
            int maxChars = (int) (width * 1000 / 12) - 3; // Subtracting 3 for the ellipsis
            if (maxChars <= 0) {
                return "..."; // Width is too small to fit even an ellipsis
            }
            return text.substring(0, Math.min(text.length(), maxChars)) + "...";
        }
        return text; // Text fits within the width
    }

    private static void drawTableRow(PDPageContentStream contentStream, float y, float margin, float tableWidth, String[] rowContent, float[] columnWidths) {
        try {
            float rowHeight = 20;
            contentStream.setFont(PDType1Font.HELVETICA, 10);

            float nextX = margin;
            float yPosition = y;

            // Draw top border for the row
            contentStream.moveTo(margin, yPosition);
            contentStream.lineTo(margin + tableWidth, yPosition);
            contentStream.stroke();

            for (int i = 0; i < rowContent.length; i++) {
                String text = rowContent[i];
                float width = columnWidths[i];
                float textWidth = PDType1Font.HELVETICA.getStringWidth(text) / 1000 * 10;

                // Adjust text positioning based on column index
                float xOffset = 5; // Padding
                if (i == 0) {
                    // Align text to the left for the first column
                    contentStream.beginText();
                    contentStream.newLineAtOffset(nextX + xOffset, yPosition - rowHeight / 2 + 5);
                } else {
                    // Center text horizontally within the column for other columns
                    contentStream.beginText();
                    contentStream.newLineAtOffset(nextX + xOffset + (width - textWidth) / 2, yPosition - rowHeight / 2 + 5);
                }
                contentStream.showText(text);
                contentStream.endText();

                // Draw right border for the column
                contentStream.moveTo(nextX + width, yPosition);
                contentStream.lineTo(nextX + width, yPosition - rowHeight);
                contentStream.stroke();

                nextX += width;
            }

            // Draw bottom border for the row
            contentStream.moveTo(margin, yPosition - rowHeight);
            contentStream.lineTo(margin + tableWidth, yPosition - rowHeight);
            contentStream.stroke();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    public static File generatePDF(List<Evenement> evenements, String outputPath) {
        File pdfFile = new File(outputPath);
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
                contentStream.setNonStrokingColor(java.awt.Color.RED);
                contentStream.beginText();
                float titleWidth = PDType1Font.HELVETICA_BOLD.getStringWidth("Liste des evenements ") / 1000f * 16;
                float titleHeight = PDType1Font.HELVETICA_BOLD.getFontDescriptor().getFontBoundingBox().getHeight() / 1000f * 16;
                contentStream.newLineAtOffset((page.getMediaBox().getWidth() - titleWidth) / 2, page.getMediaBox().getHeight() - 30 - titleHeight);
                contentStream.showText("Liste des evenements");
                contentStream.endText();

                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.setNonStrokingColor(Color.BLACK);

                float margin = 50;
                float yStart = page.getMediaBox().getHeight() - margin - 30 - titleHeight;
                float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
                float yPosition = yStart;
                float rowHeight = 20;

                // Define column widths and headers
                String[] headers = {"ID", "Titre", "Description", "Ville", "Date debut", "Nb jour", "Date fin"};
                float[] columnWidths = {25, 100, 150, 50, 80, 40, 80}; // Adjust these values as needed

                // Draw table headers
                drawTableRow(contentStream, yPosition, margin, tableWidth, headers, columnWidths);
                yPosition -= rowHeight;

                // Draw table data
                for (Evenement evenement : evenements) {
                    String[] rowData = {
                            evenement.getIdEvenement(),
                            evenement.getTitreE(),
                            evenement.getDescriptionE(),
                            evenement.getVilleE(),
                            evenement.getDateDebutE().toString(),
                            evenement.getNbJourE(),
                            evenement.getDateFinE(),

                    };
                    drawTableRow(contentStream, yPosition, margin, tableWidth, rowData, columnWidths);
                    yPosition -= rowHeight;
                }
            }

            // Save the PDF document
            document.save(pdfFile);
            Desktop.getDesktop().open(pdfFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pdfFile;
    }





}