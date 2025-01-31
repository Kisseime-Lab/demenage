package com.startup.demenage.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.startup.demenage.entity.OffreEntity;
import java.util.List;

public interface OffreRepository extends CrudRepository<OffreEntity, String> {
    Page<OffreEntity> findByAnnonceIdContainingAndAuthorContaining(String annonceId, String authorId,
            Pageable pageable);

    Page<OffreEntity> findByAuthor(String author, Pageable pageable);
}
