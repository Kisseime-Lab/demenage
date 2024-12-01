package com.startup.demenage.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.startup.demenage.entity.AdresseEntity;
import com.startup.demenage.model.Adresse;

@Component
public class AdresseDto {

    public AdresseDto() {
    }

    public Adresse toModel(AdresseEntity adresseEntity) {
        Adresse adresse = new Adresse();
        BeanUtils.copyProperties(adresseEntity, adresse);
        return adresse;
    }

    public List<Adresse> toListModel(List<AdresseEntity> adresseEntities) {
        return adresseEntities.stream().map(this::toModel).toList();
    }
    
}
