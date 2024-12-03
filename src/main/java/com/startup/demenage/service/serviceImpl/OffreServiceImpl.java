package com.startup.demenage.service.serviceImpl;

import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.startup.demenage.entity.OffreEntity;
import com.startup.demenage.model.Offre;
import com.startup.demenage.repository.OffreRepository;
import com.startup.demenage.service.OffreService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class OffreServiceImpl implements OffreService {

    private final OffreRepository repository;

    
    public OffreServiceImpl(OffreRepository repository) {
        this.repository = repository;
    }

    @Override
    public OffreEntity addUpdateOffre(Offre offre) {
        // TODO Auto-generated method stub
        OffreEntity offreEntity = this.toEntity(offre);
        if (Objects.nonNull(offre.getId()) && !Objects.equals(offre, "")) {
            Optional<OffreEntity> exOptional = repository.findById(offre.getId());
            if (exOptional.isPresent()) {
                offreEntity.setId(exOptional.get().getId());
            }
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
    public void deleteOffre(String id) {
        // TODO Auto-generated method stub
        Optional<OffreEntity> optional = repository.findById(id);
        if (optional.isEmpty()) {
            throw new EntityNotFoundException("Offre with id: " + id + " not found");
        }
        repository.delete(optional.get());
    }

    @Override
    public OffreEntity toEntity(Offre offre) {
        // TODO Auto-generated method stub
        OffreEntity offreEntity = new OffreEntity();
        String newId = offreEntity.getId();
        BeanUtils.copyProperties(offre, offreEntity);
        offreEntity.setPrix(offre.getPrix().doubleValue());
        if (Objects.nonNull(offre.getStatus())) {
            offreEntity.setStatus(offre.getStatus().name());
        }
        if (Objects.isNull(offre.getId()) || Objects.equals(offre.getId(), "")) {
            offreEntity.setId(newId);
        }
        return offreEntity;
    }

    @Override
    public Page<OffreEntity> getOffreByAnnonceId(String annonceId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return repository.findByAnnonceId(annonceId, pageable);
    }
    
}
