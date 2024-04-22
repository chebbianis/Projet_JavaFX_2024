package test;

import models.User;
import models.Voiture;
import services.UserService;
import services.VoitureService;
import utils.MyDatabase;

import java.sql.SQLException;
import java.time.LocalDate;

// Press Shift twice to open the Search Everywhere dialog and type show whitespaces,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {


        VoitureService sp = new VoitureService();

        try {
            sp.add(new Voiture("isuzuDmax", LocalDate.parse("2020-05-21"), 60, 1500, 5));
            System.out.println("Ajouter");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        /*try {
            sp.modifier(new User(1,"Koussay", "bbbb", 26,"hh","hh"));
            System.out.println("Modifier");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

//        try {
//            sp.supprimer(1);
//            System.out.println("Delate");
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }

        try {
            System.out.println(sp.recuperer());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }*/


    }
}