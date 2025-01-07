package com.startup.demenage.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class AdresseEntity {
    private String city;
    private String libelle;
    private String zipCode;


    public AdresseEntity() {
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getLibelle() {
        return libelle;
    }
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }


    public String getZipCode() {
        return zipCode;
    }


    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
    
}
