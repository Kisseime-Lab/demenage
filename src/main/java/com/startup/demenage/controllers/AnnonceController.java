package com.startup.demenage.controllers;

import static org.springframework.http.ResponseEntity.status;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.startup.demenage.AnnonceApi;
import com.startup.demenage.dto.AnnonceDto;
import com.startup.demenage.model.Annonce;
import com.startup.demenage.service.AnnonceService;

import jakarta.validation.Valid;

@Controller
public class AnnonceController implements AnnonceApi {

    private final AnnonceService service; 
    private final AnnonceDto annonceDto;

    public AnnonceController(AnnonceService service, AnnonceDto annonceDto) {
        this.service = service;
        this.annonceDto = annonceDto;
    }

    @Override
    public ResponseEntity<Annonce> createAnnonce(@Valid Annonce annonce) throws Exception {
        // TODO Auto-generated method stub
        return status(HttpStatus.CREATED).body(annonceDto.toModel(service.addUpdateAnnonce(annonce)));
    }

    @Override
    public ResponseEntity<Void> deleteAnnonce(String id) throws Exception {
        // TODO Auto-generated method stub
        service.deleteAnnonce(id);
        return status(HttpStatus.OK).build();
    }

    @Override
    public ResponseEntity<Annonce> getAnnonceById(String id) throws Exception {
        // TODO Auto-generated method stub
        return status(HttpStatus.OK).body(annonceDto.toModel(service.findAnnonce(id)));
    }
    

    @Override
    public ResponseEntity<Annonce> updateAnnonce(String id, @Valid Annonce annonce) throws Exception {
        // TODO Auto-generated method stub
        return AnnonceApi.super.updateAnnonce(id, annonce);
    }

    @Override
    public ResponseEntity<Object> getLatestAnnonce(@Valid String city, @Valid Integer page, @Valid Integer size)
            throws Exception {
        // TODO Auto-generated method stub
        return status(HttpStatus.OK).body(
            service.getLastestAnnonces(city, page, size));
    }

}
