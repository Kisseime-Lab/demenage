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

import com.startup.demenage.domain.AnnonceDomain;
import com.startup.demenage.domain.OffreDomain;
import com.startup.demenage.domain.UserDomain;
import com.startup.demenage.dto.AnnonceDto;
import com.startup.demenage.model.Offre;
import com.startup.demenage.repository.OffreRepository;
import com.startup.demenage.service.AnnonceService;
import com.startup.demenage.service.OffreService;
import com.startup.demenage.service.UserService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class OffreServiceImpl implements OffreService {

    private final OffreRepository repository;
    private final AnnonceService annonceService;
    private final AnnonceDto annonceDto;
    private final UserService userService;

    public OffreServiceImpl(OffreRepository repository, AnnonceService annonceService, AnnonceDto annoncedto,
            UserService userService) {
        this.repository = repository;
        this.annonceService = annonceService;
        this.annonceDto = annoncedto;
        this.userService = userService;
    }

    @Override
    public OffreDomain addUpdateOffre(Offre offre) {
        // TODO Auto-generated method stub
        OffreDomain offreEntity = this.toEntity(offre);
        List<OffreDomain> exAnnonceDomain = repository
                .findByAnnonce_IdContainingAndAuthor_IdContaining(offre.getAnnonce().getId(),
                        offre.getAuthor().getId(), Pageable.ofSize(1))
                .getContent();
        if (exAnnonceDomain.size() > 0) {
            offreEntity.setId(exAnnonceDomain.get(0).getId());
            if (!Objects.equals(offreEntity.isDeleted(), exAnnonceDomain.get(0).isDeleted())) {
                offreEntity.setDeletedAt(LocalDateTime.now().toString());
            }
        } else {
            AnnonceDomain annonceEntity = annonceService.findAnnonce(offre.getAnnonce().getId(), null);
            annonceEntity.setOffres(annonceEntity.getOffres() + 1);
            annonceService.addUpdateAnnonce(annonceDto.toModel(annonceEntity));
        }
        return repository.save(offreEntity);
    }

    @Override
    public OffreDomain findById(String id) {
        Optional<OffreDomain> optional = repository.findById(id);
        if (!optional.isPresent()) {
            throw new EntityNotFoundException("Offre not found");
        }
        return optional.get();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteOffre(String id) {
        // TODO Auto-generated method stub
        Optional<OffreDomain> optional = repository.findById(id);
        if (optional.isEmpty()) {
            throw new EntityNotFoundException("Offre with id: " + id + " not found");
        }
        AnnonceDomain annonceEntity = annonceService.findAnnonce(optional.get().getAnnonce().getId(), "true");
        annonceEntity.setOffres(annonceEntity.getOffres() - 1);
        annonceService.addUpdateAnnonce(annonceDto.toModel(annonceEntity));
        repository.delete(optional.get());
    }

    @Override
    public OffreDomain toEntity(Offre offre) {
        // TODO Auto-generated method stub
        OffreDomain offreEntity = new OffreDomain();
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
        AnnonceDomain annonceEntity = annonceService.findAnnonce(offre.getAnnonce().getId(), null);
        offreEntity.setAnnonce(annonceEntity);
        UserDomain userEntity = userService.getUserById(offre.getAuthor().getId(), null);
        offreEntity.setAuthor(userEntity);
        return offreEntity;
    }

    @Override
    public Page<OffreDomain> getOffreByAnnonceIdAndAuthorId(String annonceId, String authorId, int page, int size,
            String byAdmin) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        // if (Objects.nonNull(annonceId) || !Objects.equals(annonceId, "")) {
        // Page<OffreDomain> result =
        // repository.findByAnnonceIdAndAuthorContaining(annonceId, authorId, pageable);
        // if (Objects.isNull(byAdmin)) {
        // List<OffreDomain> offres = result.getContent().stream().filter(a ->
        // !a.isDeleted()).toList();
        // return new PageImpl<>(offres, pageable, offres.size());
        // }
        // return result;
        // }
        Page<OffreDomain> result = repository.findByAnnonce_IdContainingAndAuthor_IdContaining(annonceId, authorId,
                pageable);
        if (Objects.isNull(byAdmin)) {
            List<OffreDomain> offres = result.getContent().stream().filter(a -> !a.isDeleted()).toList();
            return new PageImpl<>(offres, pageable, offres.size());
        }
        return result;
        // if (Objects.nonNull(authorId) && !Objects.equals(authorId, "")) {
        // Page<OffreDomain> result = repository.findByAuthor(authorId, pageable);
        // if (Objects.isNull(byAdmin)) {
        // List<OffreDomain> offres = result.getContent().stream().filter(a ->
        // !a.isDeleted()).toList();
        // return new PageImpl<>(offres, pageable, offres.size());
        // }
        // return result;
        // }
        // return null;
    }
}
