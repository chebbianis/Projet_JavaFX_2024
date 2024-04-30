package test;

import models.Annonce;
import services.AnnonceService;

public class Main {

    public static void main(String[] args) {
        try {
            AnnonceService as = new AnnonceService();

            Annonce p1 = new Annonce("Annonce1", "This is the first annonce", "Douz", "2023-12-12", "3", "2023-12-15");

            //as.addAnnonce(p1);

            // Correcting the parameters for updateAnnonce
            Annonce updatedAnnonce = new Annonce("Annonce1", "This is the first annonce", "Tunis", "2023-12-12", "3", "2023-12-15");
            //as.updateAnnonce(updatedAnnonce);

            System.out.println(as.allData());
        } catch (RuntimeException e) { // Catch RuntimeException instead
            // Handle RuntimeException
            System.err.println("Exception occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
