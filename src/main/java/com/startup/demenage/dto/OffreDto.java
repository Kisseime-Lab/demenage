package com.startup.demenage.dto;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.startup.demenage.entity.OffreEntity;
import com.startup.demenage.model.Offre;
import com.startup.demenage.model.Offre.StatusEnum;

@Component
public class OffreDto {

    private final UserDto userDto;
    private final AnnonceDto annonceDto;

    public OffreDto(UserDto userDto, AnnonceDto annonceDto) {
        this.userDto = userDto;
        this.annonceDto = annonceDto;
    }

    public Offre toModel(OffreEntity offreEntity) {
        Offre offre = new Offre();
        BeanUtils.copyProperties(offreEntity, offre);
        offre.setCreatedAt(offreEntity.getCreatedAt().toString());
        offre.setPrix(BigDecimal.valueOf(offreEntity.getPrix()));
        offre.setDate(offreEntity.getDate());
        offre.setStatus(StatusEnum.fromValue(offreEntity.getStatus()));
        offre.setAnnonce(annonceDto.toModel(offreEntity.getAnnonce()));
        offre.setAuthor(userDto.toModel(offreEntity.getAuthor()));
        return offre;
    }

    public Page<Offre> toListModel(Page<OffreEntity> offreEntities) {
        return offreEntities.map(o -> toModel(o));
    }
}
