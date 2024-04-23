package services;
import java.sql.SQLException;
import java.util.List;



    public interface  IServices <T>{

            void ajouter(T objet) throws SQLException;
            void modifier(T objet) throws SQLException;
            void supprimer(T objet) throws SQLException;
            List<T> afficher() throws SQLException;

    }

