package com.startup.demenage.service.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.startup.demenage.dto.AnnonceDto;
import com.startup.demenage.entity.AnnonceEntity;
import com.startup.demenage.entity.OffreEntity;
import com.startup.demenage.model.Offre;
import com.startup.demenage.repository.OffreRepository;
import com.startup.demenage.service.AnnonceService;
import com.startup.demenage.service.OffreService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class OffreServiceImpl implements OffreService {

    private final OffreRepository repository;
    private final AnnonceService annonceService;
    private final AnnonceDto annonceDto;

    public OffreServiceImpl(OffreRepository repository, AnnonceService annonceService, AnnonceDto annoncedto) {
        this.repository = repository;
        this.annonceService = annonceService;
        this.annonceDto = annoncedto;
    }

    @Override
    public OffreEntity addUpdateOffre(Offre offre) {
        // TODO Auto-generated method stub
        OffreEntity offreEntity = this.toEntity(offre);
        if (Objects.nonNull(offre.getId()) && !Objects.equals(offre.getId(), "")) {
            Optional<OffreEntity> exOptional = repository.findById(offre.getId());
            if (exOptional.isPresent()) {
                offreEntity.setId(exOptional.get().getId());
                if (!Objects.equals(offreEntity.isDeleted(), exOptional.get().isDeleted())) {
                    offreEntity.setDeletedAt(LocalDateTime.now().toString());
                }

            }
        } else {
            AnnonceEntity annonceEntity = annonceService.findAnnonce(offre.getAnnonceId(), null);
            annonceEntity.setOffres(annonceEntity.getOffres() + 1);
            annonceService.addUpdateAnnonce(annonceDto.toModel(annonceEntity));
        }
        return repository.save(offreEntity);
    }

    @Override
    public OffreEntity findById(String id) {
        Optional<OffreEntity> optional = repository.findById(id);
        if (!optional.isPresent()) {
            throw new EntityNotFoundException("Offre not found");
        }
        return optional.get();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteOffre(String id) {
        // TODO Auto-generated method stub
        Optional<OffreEntity> optional = repository.findById(id);
        if (optional.isEmpty()) {
            throw new EntityNotFoundException("Offre with id: " + id + " not found");
        }
        AnnonceEntity annonceEntity = annonceService.findAnnonce(optional.get().getAnnonceId(), "true");
        annonceEntity.setOffres(annonceEntity.getOffres() - 1);
        annonceService.addUpdateAnnonce(annonceDto.toModel(annonceEntity));
        repository.delete(optional.get());
    }

    @Override
    public OffreEntity toEntity(Offre offre) {
        // TODO Auto-generated method stub
        OffreEntity offreEntity = new OffreEntity();
        String newId = offreEntity.getId();
        BeanUtils.copyProperties(offre, offreEntity);
        offreEntity.setPrix(offre.getPrix().doubleValue());
        offreEntity.setDate(offre.getDate());
        if (Objects.nonNull(offre.getStatus())) {
            offreEntity.setStatus(offre.getStatus().name());
        }
        if (Objects.isNull(offre.getId()) || Objects.equals(offre.getId(), "")) {
            offreEntity.setId(newId);
        }
        return offreEntity;
    }

    @Override
    public Page<OffreEntity> getOffreByAnnonceId(String annonceId, String authorId, int page, int size,
            String byAdmin) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        // if (Objects.nonNull(annonceId) || !Objects.equals(annonceId, "")) {
        // Page<OffreEntity> result =
        // repository.findByAnnonceIdAndAuthorContaining(annonceId, authorId, pageable);
        // if (Objects.isNull(byAdmin)) {
        // List<OffreEntity> offres = result.getContent().stream().filter(a ->
        // !a.isDeleted()).toList();
        // return new PageImpl<>(offres, pageable, offres.size());
        // }
        // return result;
        // }
        Page<OffreEntity> result = repository.findByAnnonceIdContainingAndAuthorContaining(annonceId, authorId,
                pageable);
        if (Objects.isNull(byAdmin)) {
            List<OffreEntity> offres = result.getContent().stream().filter(a -> !a.isDeleted()).toList();
            return new PageImpl<>(offres, pageable, offres.size());
        }
        return result;
        // if (Objects.nonNull(authorId) && !Objects.equals(authorId, "")) {
        // Page<OffreEntity> result = repository.findByAuthor(authorId, pageable);
        // if (Objects.isNull(byAdmin)) {
        // List<OffreEntity> offres = result.getContent().stream().filter(a ->
        // !a.isDeleted()).toList();
        // return new PageImpl<>(offres, pageable, offres.size());
        // }
        // return result;
        // }
        // return null;
    }
}
