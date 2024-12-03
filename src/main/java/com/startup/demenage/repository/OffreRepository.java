package com.startup.demenage.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.startup.demenage.entity.OffreEntity;

public interface OffreRepository extends CrudRepository<OffreEntity, String> {
    Page<OffreEntity> findByAnnonceId(String annonceId, Pageable pageable);
}
