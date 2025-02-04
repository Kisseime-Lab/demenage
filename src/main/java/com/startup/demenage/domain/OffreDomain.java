package com.startup.demenage.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class OffreDomain {

    private String id;
    private double prix;
    private UserDomain author;
    private AnnonceDomain annonce;
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

    private String status;
    private LocalDateTime createdAt;

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffreDomain() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.status = "PENDING";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public AnnonceDomain getAnnonce() {
        return annonce;
    }

    public void setAnnonce(AnnonceDomain annonce) {
        this.annonce = annonce;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UserDomain getAuthor() {
        return author;
    }

    public void setAuthor(UserDomain author) {
        this.author = author;
    }

}
