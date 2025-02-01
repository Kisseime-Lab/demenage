package com.startup.demenage.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.startup.demenage.entity.AnnonceEntity;

public interface AnnonceRepository extends JpaRepository<AnnonceEntity, String> {

    Page<AnnonceEntity> findByDepartureCityContainingAndDestinationCityContaining(
            String cityDepart, String cityDestination, Pageable pageable);

    Page<AnnonceEntity> findByAuthor_Id(String author, Pageable pageable);
}
