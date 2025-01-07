package com.startup.demenage.service;

import org.springframework.data.domain.Page;

import com.startup.demenage.entity.OffreEntity;
import com.startup.demenage.model.Offre;

public interface OffreService {
   OffreEntity addUpdateOffre(Offre offre);
   OffreEntity findById(String id);
   void deleteOffre(String id);
   OffreEntity toEntity(Offre offre);
   Page<OffreEntity> getOffreByAnnonceId(String annonceId, String authorId, int page, int size, String byAdmin);
}
