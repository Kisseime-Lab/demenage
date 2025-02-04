package com.startup.demenage.service;

import org.springframework.data.domain.Page;

import com.startup.demenage.domain.AdresseDomain;
import com.startup.demenage.domain.AnnonceDomain;
import com.startup.demenage.model.Adresse;
import com.startup.demenage.model.Annonce;

public interface AnnonceService {
    Page<AnnonceDomain> getLastestAnnonces(String cityDepart, String cityDestination, String authorId, int page,
            int size,
            String byAdmin);

    AnnonceDomain addUpdateAnnonce(Annonce annonce);

    void deleteAnnonce(String id, String byAdmin);

    AnnonceDomain toEntity(Annonce annonce);

    AnnonceDomain findAnnonce(String id, String byAdmin);

    AdresseDomain adresseToEntity(Adresse adresse);
}
