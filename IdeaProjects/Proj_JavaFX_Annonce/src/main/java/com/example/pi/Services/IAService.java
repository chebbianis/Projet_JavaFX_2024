package com.example.pi.Services;
import com.example.pi.Entities.Annonce;

public interface IAService {
    Annonce afficherAnnonce(int id);
    void ajouterAnnonce(Annonce annonce);
    void modifierAnnonce(Annonce annonce);
    void supprimerAnnonce(int id);
}