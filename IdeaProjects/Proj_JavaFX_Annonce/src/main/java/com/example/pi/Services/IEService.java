package com.example.pi.Services;

import com.example.pi.Entities.Evenement;

public interface IEService {
    Evenement afficherEvenement(int id_evenement);
    void ajouterEvenement(Evenement evenement);
    void modifierEvenement(Evenement evenement);

    void supprimerEvenement(int id);
}