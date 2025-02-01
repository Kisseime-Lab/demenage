package com.startup.demenage.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import com.startup.demenage.entity.AnnonceEntity;
import com.startup.demenage.model.Annonce;
import com.startup.demenage.model.User;

@Component
final public class AnnonceDto {

    private final AdresseDto adresseDto;
    private final UserDto userDto;

    public AnnonceDto(AdresseDto adresseDto, UserDto userDto) {
        this.adresseDto = adresseDto;
        this.userDto = userDto;
    }

    public Annonce toModel(AnnonceEntity annonceEntity) {
        Annonce annonce = new Annonce();
        BeanUtils.copyProperties(annonceEntity, annonce);
        annonce.setDistance(BigDecimal.valueOf(annonceEntity.getDistance()));
        annonce.setPrix(BigDecimal.valueOf(annonceEntity.getPrix()));
        annonce.setCreatedAt(annonceEntity.getCreatedAt().toString());
        annonce.setDeparture(adresseDto.toModel(annonceEntity.getDeparture()));
        annonce.setDestination(adresseDto.toModel(annonceEntity.getDestination()));
        annonce.setOffres(BigDecimal.valueOf(annonceEntity.getOffres()));
        User user = userDto.toModel(annonceEntity.getAuthor());
        annonce.setAuthor(user);
        return annonce;
    }

    public Page<Annonce> toListModel(Page<AnnonceEntity> annonces) {
        return annonces.map(a -> toModel(a));
    }
}
