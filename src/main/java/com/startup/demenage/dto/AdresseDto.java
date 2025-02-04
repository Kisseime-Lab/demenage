package com.startup.demenage.dto;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.startup.demenage.domain.AdresseDomain;
import com.startup.demenage.model.Adresse;

@Component
public class AdresseDto {

    public AdresseDto() {
    }

    public Adresse toModel(AdresseDomain adresseDomain) {
        Adresse adresse = new Adresse();
        BeanUtils.copyProperties(adresseDomain, adresse);
        return adresse;
    }

    public List<Adresse> toListModel(List<AdresseDomain> adresseEntities) {
        return adresseEntities.stream().map(this::toModel).toList();
    }

}
