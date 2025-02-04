package com.startup.demenage.repository.Jpa.data;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Offre")
public class OffreEntity {

    @Id
    private String id;
    private double prix;
    @ManyToOne(optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private UserEntity author;
    @ManyToOne(optional = false)
    @JoinColumn(name = "annonce_id", nullable = false)
    private AnnonceEntity annonce;
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

    public OffreEntity() {
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

    public AnnonceEntity getAnnonce() {
        return annonce;
    }

    public void setAnnonce(AnnonceEntity annonce) {
        this.annonce = annonce;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UserEntity getAuthor() {
        return author;
    }

    public void setAuthor(UserEntity author) {
        this.author = author;
    }

}
