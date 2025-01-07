package com.startup.demenage.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Annonce")
public class AnnonceEntity {
    @Id
    private String id;
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "city", column = @Column(name = "ville_depart")),
        @AttributeOverride(name = "libelle", column = @Column(name = "libelle_adresse_depart")),
        @AttributeOverride(name = "zipCode", column = @Column(name = "code_postal_depart"))
    })
    private AdresseEntity departure;
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "city", column = @Column(name = "ville_arrivee")),
        @AttributeOverride(name = "libelle", column = @Column(name = "libelle_adresse_arrivee")),
        @AttributeOverride(name = "zipCode", column = @Column(name = "code_postal_arrivee"))
    })
    private AdresseEntity destination;
    @ElementCollection
    @CollectionTable
    private List<String> photos;
    private double distance;
    private double prix;
    private String author;
    private boolean open;
    private String titre;
    private int offres;
    private String date;
    private boolean deleted = false;
    private String deletedAt;
    
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }
    
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public int getOffres() {
        return offres;
    }
    public void setOffres(int offres) {
        this.offres = offres;
    }
    public String getTitre() {
        return titre;
    }
    public void setTitre(String titre) {
        this.titre = titre;
    }
    private LocalDateTime createdAt;
    
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public AnnonceEntity() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public AdresseEntity getDeparture() {
        return departure;
    }
    public void setDeparture(AdresseEntity departure) {
        this.departure = departure;
    }
    public AdresseEntity getDestination() {
        return destination;
    }
    public void setDestination(AdresseEntity destination) {
        this.destination = destination;
    }
    public List<String> getPhotos() {
        return photos;
    }
    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }
    public double getDistance() {
        return distance;
    }
    public void setDistance(double distance) {
        this.distance = distance;
    }
    public double getPrix() {
        return prix;
    }
    public void setPrix(double prix) {
        this.prix = prix;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public boolean isOpen() {
        return open;
    }
    public void setOpen(boolean open) {
        this.open = open;
    }

    
}