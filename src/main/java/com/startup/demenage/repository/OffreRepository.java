package com.startup.demenage.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.startup.demenage.domain.OffreDomain;

public interface OffreRepository {
    Page<OffreDomain> findByAnnonce_IdContainingAndAuthor_IdContaining(String annonceId, String authorId,
            Pageable pageable);

    Page<OffreDomain> findByAuthor_Id(String author, Pageable pageable);

    OffreDomain save(OffreDomain offreDomain);

    Optional<OffreDomain> findById(String id);

    void delete(OffreDomain offre);
}
