package com.example.pi.Controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RatingDAO {

    private final String url = "jdbc:mysql://localhost:3306/tunvista_integration";
    private final String user = "root";
    private final String password = "mohamedomar";

    public void mettreAJourNoteLivreur(int refB, int nouvelleNote) {
        String query = "UPDATE maison SET maps_link = ? WHERE ref_b = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, nouvelleNote);
            pstmt.setInt(2, refB);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

