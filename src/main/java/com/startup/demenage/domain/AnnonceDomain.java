package com.startup.demenage.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class AnnonceDomain {
    private String id;
    private AdresseDomain departure;
    private AdresseDomain destination;
    private String photos;
    private double distance;
    private double prix;

    private UserDomain author;
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

    private String createdAt;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public AnnonceDomain() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AdresseDomain getDeparture() {
        return departure;
    }

    public void setDeparture(AdresseDomain departure) {
        this.departure = departure;
    }

    public AdresseDomain getDestination() {
        return destination;
    }

    public void setDestination(AdresseDomain destination) {
        this.destination = destination;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
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

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public UserDomain getAuthor() {
        return author;
    }

    public void setAuthor(UserDomain author) {
        this.author = author;
    }

}