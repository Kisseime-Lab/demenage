package com.startup.demenage.controllers;

import static org.springframework.http.ResponseEntity.status;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.startup.demenage.OffreApi;
import com.startup.demenage.dto.OffreDto;
import com.startup.demenage.model.Offre;
import com.startup.demenage.service.OffreService;

import jakarta.validation.Valid;

@Controller
public class OffreController implements OffreApi {

    private final OffreDto offreDto;
    private final OffreService service;

    public OffreController(OffreDto offreDto, OffreService service) {
        this.offreDto = offreDto;
        this.service = service;
    }

    @Override
    public ResponseEntity<Offre> addUpdateOffre(@Valid Offre offre) throws Exception {
        HttpStatus status = Objects.isNull(offre.getId()) ? HttpStatus.CREATED : HttpStatus.OK;
        return status(status).body(offreDto.toModel(service.addUpdateOffre(offre)));
    }

    @Override
    public ResponseEntity<Void> deleteOffre(String id) throws Exception {
        service.deleteOffre(id);
        return status(HttpStatus.OK).build();
    }


    @Override
    public ResponseEntity<Offre> getOffreById(String id) throws Exception {
        return OffreApi.super.getOffreById(id);
    }

    @Override
    public ResponseEntity<Object> getOffreByAnnonceId(@Valid String annonceId, @Valid String authorId,
            @Valid Integer page, @Valid Integer size) throws Exception {
        return status(HttpStatus.OK).body(service.getOffreByAnnonceId(annonceId, authorId, page, size));
    }

    @Override
    public ResponseEntity<Offre> updateOffre(String id) throws Exception {
        return OffreApi.super.updateOffre(id);
    }
    
}
