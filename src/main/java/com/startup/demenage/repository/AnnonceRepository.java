package com.startup.demenage.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.startup.demenage.entity.AnnonceEntity;

public interface AnnonceRepository extends JpaRepository<AnnonceEntity, String> {
    
    Page<AnnonceEntity> findByDepartureCityContaining(String city, Pageable pageable);
}
