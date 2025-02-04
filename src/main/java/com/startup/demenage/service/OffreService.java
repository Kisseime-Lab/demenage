package com.startup.demenage.service;

import org.springframework.data.domain.Page;

import com.startup.demenage.domain.OffreDomain;
import com.startup.demenage.model.Offre;

public interface OffreService {
      OffreDomain addUpdateOffre(Offre offre);

      OffreDomain findById(String id);

      void deleteOffre(String id);

      OffreDomain toEntity(Offre offre);

      Page<OffreDomain> getOffreByAnnonceIdAndAuthorId(String annonceId, String authorId, int page, int size,
                  String byAdmin);
}
