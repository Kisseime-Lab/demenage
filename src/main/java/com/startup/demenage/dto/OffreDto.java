package com.startup.demenage.dto;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.startup.demenage.entity.OffreEntity;
import com.startup.demenage.model.Offre;
import com.startup.demenage.model.Offre.StatusEnum;

@Component
public class OffreDto {
    public Offre toModel(OffreEntity offreEntity) {
        Offre offre = new Offre();
        BeanUtils.copyProperties(offreEntity, offre);
        offre.setCreatedAt(offreEntity.getCreatedAt().toString());
        offre.setPrix(BigDecimal.valueOf(offreEntity.getPrix()));
        offre.setStatus(StatusEnum.fromValue(offreEntity.getStatus()));
        return offre;
    }

    public List<Offre> toListModel(List<OffreEntity> offreEntities) {
        return offreEntities.stream().map(this::toModel).toList();
    }
}
