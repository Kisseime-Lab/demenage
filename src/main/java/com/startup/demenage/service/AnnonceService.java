package com.startup.demenage.service;

import org.springframework.data.domain.Page;

import com.startup.demenage.entity.AdresseEntity;
import com.startup.demenage.entity.AnnonceEntity;
import com.startup.demenage.model.Adresse;
import com.startup.demenage.model.Annonce;

public interface AnnonceService {
    Page<AnnonceEntity> getLastestAnnonces(String city, int page, int size);
    AnnonceEntity addUpdateAnnonce(Annonce annonce);
    void deleteAnnonce(String id);
    AnnonceEntity toEntity(Annonce annonce);
    AnnonceEntity findAnnonce(String id);
    AdresseEntity adresseToEntity(Adresse adresse);
}
