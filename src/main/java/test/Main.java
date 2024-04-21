package test;
import entities.Maison;
import entities.Visit;
import services.ServiceMaison;
import services.ServiceVisit;
import utils.MyDB;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {
        MyDB db1= MyDB.getInstance();
        Maison maison= new Maison("maleeeek","Monastir",6,2500,"Appartement","blaaa");
        // maison.setRefB(15);
        LocalDate date1 = LocalDate.of(2024, 04, 5);
        Visit visit= new Visit(99687111,"nourhenne","malekbdiri05@gmail.com",20, date1);
        ServiceMaison serviceMaison=new ServiceMaison ();
        ServiceVisit serviceVisit=new ServiceVisit ();
        try {
             //serviceMaison.ajouter(maison);
             //serviceMaison.modifier(maison);
           // System.out.println(serviceVisit.afficher());
            serviceVisit.ajouter(visit);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    }





