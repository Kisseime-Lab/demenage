package com.startup.demenage.service;

import org.springframework.data.domain.Page;

import com.startup.demenage.entity.AdresseEntity;
import com.startup.demenage.entity.AnnonceEntity;
import com.startup.demenage.model.Adresse;
import com.startup.demenage.model.Annonce;

public interface AnnonceService {
    Page<AnnonceEntity> getLastestAnnonces(String cityDepart, String cityDestination, String authorId, int page, int size, String byAdmin);
    AnnonceEntity addUpdateAnnonce(Annonce annonce);
    void deleteAnnonce(String id, String byAdmin);
    AnnonceEntity toEntity(Annonce annonce);
    AnnonceEntity findAnnonce(String id, String byAdmin);
    AdresseEntity adresseToEntity(Adresse adresse);
}
