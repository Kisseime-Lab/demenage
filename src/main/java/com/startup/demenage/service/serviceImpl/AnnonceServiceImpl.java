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

import com.startup.demenage.domain.AdresseDomain;
import com.startup.demenage.domain.AnnonceDomain;
import com.startup.demenage.domain.UserDomain;
import com.startup.demenage.model.Adresse;
import com.startup.demenage.model.Annonce;
import com.startup.demenage.repository.AnnonceRepository;
import com.startup.demenage.service.AnnonceService;
import com.startup.demenage.service.UserService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class AnnonceServiceImpl implements AnnonceService {

    private final AnnonceRepository repository;
    private final UserService uService;

    public AnnonceServiceImpl(AnnonceRepository repository, UserService uService) {
        this.repository = repository;
        this.uService = uService;
    }

    @Override
    public AnnonceDomain addUpdateAnnonce(Annonce annonce) {
        AnnonceDomain annonceDomain = this.toEntity(annonce);
        if (Objects.nonNull(annonce.getId()) && !Objects.equals(annonce, "")) {
            Optional<AnnonceDomain> exOptional = repository.findById(annonce.getId());
            if (exOptional.isPresent()) {
                annonceDomain.setId(exOptional.get().getId());
                if (!Objects.equals(annonceDomain.isDeleted(), exOptional.get().isDeleted())) {
                    annonceDomain.setDeletedAt(LocalDateTime.now().toString());
                }
            }
        }
        return repository.save(annonceDomain);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAnnonce(String id, String byAdmin) {
        // TODO Auto-generated method stub
        Optional<AnnonceDomain> optional = repository.findById(id);
        if (optional.isEmpty()) {
            throw new EntityNotFoundException("Annonce with id: " + id + " not found");
        }
        if (optional.get().isDeleted() && Objects.isNull(byAdmin)) {
            throw new EntityNotFoundException("Annonce with id: " + id + " not found");
        }
        if (Objects.nonNull(byAdmin)) {
            repository.delete(optional.get());
        }
    }

    @Override
    public AnnonceDomain toEntity(Annonce annonce) {
        // TODO Auto-generated method stub
        AnnonceDomain annonceDomain = new AnnonceDomain();
        String newId = annonceDomain.getId();
        BeanUtils.copyProperties(annonce, annonceDomain);
        if (Objects.isNull(annonce.getId()) || Objects.equals(annonce.getId(), "")) {
            annonceDomain.setId(newId);
        }
        annonceDomain.setOffres(annonce.getOffres().intValue());
        annonceDomain.setPrix(annonce.getPrix().doubleValue());
        annonceDomain.setDistance(annonce.getDistance().doubleValue());
        annonceDomain.setDate(annonce.getDate());
        annonceDomain.setDeparture(this.adresseToEntity(annonce.getDeparture()));
        annonceDomain.setDestination(this.adresseToEntity(annonce.getDestination()));
        UserDomain userEntity = uService.getUserById(annonce.getAuthor().getId(), null);
        annonceDomain.setAuthor(userEntity);
        return annonceDomain;
    }

    @Override
    public AdresseDomain adresseToEntity(Adresse adresse) {
        // TODO Auto-generated method stub
        AdresseDomain adresseEntity = new AdresseDomain();
        BeanUtils.copyProperties(adresse, adresseEntity);
        return adresseEntity;
    }

    @Override
    public AnnonceDomain findAnnonce(String id, String byAdmin) {
        // TODO Auto-generated method stub
        Optional<AnnonceDomain> optional = repository.findById(id);
        if (!optional.isPresent()) {
            throw new EntityNotFoundException("Annonce not found");
        }
        if (optional.get().isDeleted() && Objects.isNull(byAdmin)) {
            throw new EntityNotFoundException("Annonce not found");
        }
        return optional.get();
    }

    @Override
    public Page<AnnonceDomain> getLastestAnnonces(String cityDepart, String cityDestination, String authorId, int page,
            int size, String byAdmin) {
        // TODO Auto-generated method stub
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        if (!Objects.equals(authorId, "")) {
            return repository.findByAuthor_Id(authorId, pageable);
        }
        Page<AnnonceDomain> result = repository.findByDepartureCityContainingAndDestinationCityContaining(
                cityDepart, cityDestination, pageable);
        if (Objects.isNull(byAdmin)) {
            List<AnnonceDomain> annonces = result.getContent().stream().filter(a -> !a.isDeleted()).toList();
            return new PageImpl<>(annonces, pageable, annonces.size());
        }
        return result;
    }
}
