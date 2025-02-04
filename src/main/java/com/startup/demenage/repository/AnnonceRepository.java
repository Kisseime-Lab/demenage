package com.startup.demenage.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.startup.demenage.domain.AnnonceDomain;

public interface AnnonceRepository {
    Optional<AnnonceDomain> findById(String id);

    Page<AnnonceDomain> findByAuthor_Id(String id, Pageable pageable);

    AnnonceDomain save(AnnonceDomain annonceEntity);

    void delete(AnnonceDomain aEntity);

    Page<AnnonceDomain> findByDepartureCityContainingAndDestinationCityContaining(
            String cityDepart, String cityDestination, Pageable pageable);
}
