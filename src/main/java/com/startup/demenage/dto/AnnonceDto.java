package com.startup.demenage.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.startup.demenage.entity.AnnonceEntity;
import com.startup.demenage.model.Annonce;

@Component
public class AnnonceDto {

    private AdresseDto adresseDto;

    public AnnonceDto(AdresseDto adresseDto) {
        this.adresseDto = adresseDto;
    }

    public Annonce toModel(AnnonceEntity annonceEntity) {
        Annonce annonce = new Annonce();
        BeanUtils.copyProperties(annonceEntity, annonce);
        annonce.setDistance(BigDecimal.valueOf(annonceEntity.getDistance()));
        annonce.setPrix(BigDecimal.valueOf(annonceEntity.getPrix()));
        annonce.setCreatedAt(annonceEntity.getCreatedAt().toString());
        annonce.setDeparture(adresseDto.toModel(annonceEntity.getDeparture()));
        annonce.setDestination(adresseDto.toModel(annonceEntity.getDestination()));
        return annonce;
    }

    public List<Annonce> toListModel(List<AnnonceEntity> userEntityList) {
        return userEntityList.stream().map(this::toModel).toList();
    }
}
