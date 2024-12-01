package com.startup.demenage.service.serviceImpl;

import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.startup.demenage.entity.AdresseEntity;
import com.startup.demenage.entity.AnnonceEntity;
import com.startup.demenage.model.Adresse;
import com.startup.demenage.model.Annonce;
import com.startup.demenage.repository.AnnonceRepository;
import com.startup.demenage.service.AnnonceService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AnnonceServiceImpl implements AnnonceService {

    private AnnonceRepository repository;

    public AnnonceServiceImpl(AnnonceRepository repository) {
        this.repository = repository;
    }

    @Override
    public AnnonceEntity addUpdateAnnonce(Annonce annonce) {
        AnnonceEntity annonceEntity = this.toEntity(annonce);
        if (Objects.nonNull(annonce.getId()) && !Objects.equals(annonce, "")) {
            Optional<AnnonceEntity> exOptional = repository.findById(annonce.getId());
            if (exOptional.isPresent()) {
                annonceEntity.setId(exOptional.get().getId());
            }
        }
        return repository.save(annonceEntity);
    }

    @Override
    public void deleteAnnonce(String id) {
        // TODO Auto-generated method stub
        Optional<AnnonceEntity> optional = repository.findById(id);
        if (optional.isEmpty()) {
            throw new EntityNotFoundException("Annonce with id: " + id + " not found");
        }
        repository.delete(optional.get());
    }

    @Override
    public AnnonceEntity toEntity(Annonce annonce) {
        // TODO Auto-generated method stub
        AnnonceEntity annonceEntity = new AnnonceEntity();
        String newId = annonceEntity.getId();
        BeanUtils.copyProperties(annonce, annonceEntity);
        if (Objects.isNull(annonce.getId()) || Objects.equals(annonce.getId(), "")) {
            annonceEntity.setId(newId);
        }
        annonceEntity.setPrix(annonce.getPrix().doubleValue());
        annonceEntity.setDistance(annonce.getDistance().doubleValue());
        annonceEntity.setDeparture(this.adresseToEntity(annonce.getDeparture()));
        annonceEntity.setDestination(this.adresseToEntity(annonce.getDestination()));
        return annonceEntity;
    }

    

    @Override
    public AdresseEntity adresseToEntity(Adresse adresse) {
        // TODO Auto-generated method stub
        AdresseEntity adresseEntity = new AdresseEntity();
        BeanUtils.copyProperties(adresse, adresseEntity);
        return adresseEntity;
    }

    @Override
    public AnnonceEntity findAnnonce(String id) {
        // TODO Auto-generated method stub
        Optional<AnnonceEntity> optional = repository.findById(id);
        if (!optional.isPresent()) {
            throw new EntityNotFoundException("Annonce not found");
        }
        return optional.get();
    }

    @Override
    public Page<AnnonceEntity> getLastestAnnonces(String city, int page, int size) {
        // TODO Auto-generated method stub
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return repository.findByDepartureCityContaining(city, pageable);
    }

    
}
