package com.startup.demenage.repository.Jpa.mappers;

import com.startup.demenage.domain.AdresseDomain;
import com.startup.demenage.domain.AnnonceDomain;
import com.startup.demenage.domain.OffreDomain;
import com.startup.demenage.domain.UserDomain;
import com.startup.demenage.domain.UserTokenDomain;
import com.startup.demenage.repository.Jpa.data.AdresseEntity;
import com.startup.demenage.repository.Jpa.data.AnnonceEntity;
import com.startup.demenage.repository.Jpa.data.OffreEntity;
import com.startup.demenage.repository.Jpa.data.UserEntity;
import com.startup.demenage.repository.Jpa.data.UserTokenEntity;

public class JpaMapper {
    public static AdresseEntity adresseToEntity(AdresseDomain domain) {
        if (domain == null) {
            return null;
        }
        AdresseEntity entity = new AdresseEntity();
        entity.setCity(domain.getCity());
        entity.setLibelle(domain.getLibelle());
        entity.setZipCode(domain.getZipCode());
        return entity;
    }

    // Convertir AdresseEntity en AdresseDomain
    public static AdresseDomain adresseToDomain(AdresseEntity entity) {
        if (entity == null) {
            return null;
        }
        AdresseDomain domain = new AdresseDomain();
        domain.setCity(entity.getCity());
        domain.setLibelle(entity.getLibelle());
        domain.setZipCode(entity.getZipCode());
        return domain;
    }

    public static AnnonceEntity annonceToEntity(AnnonceDomain domain) {
        if (domain == null) {
            return null;
        }

        AnnonceEntity entity = new AnnonceEntity();
        entity.setId(domain.getId());
        entity.setDeparture(adresseToEntity(domain.getDeparture()));
        entity.setDestination(adresseToEntity(domain.getDestination()));
        entity.setPhotos(domain.getPhotos());
        entity.setDistance(domain.getDistance());
        entity.setPrix(domain.getPrix());
        entity.setAuthor(userToEntity(domain.getAuthor()));
        entity.setOpen(domain.isOpen());
        entity.setTitre(domain.getTitre());
        entity.setOffres(domain.getOffres());
        entity.setDate(domain.getDate());
        entity.setDeleted(domain.isDeleted());
        entity.setDeletedAt(domain.getDeletedAt());

        return entity;
    }

    public static AnnonceDomain annonceToDomain(AnnonceEntity entity) {
        if (entity == null) {
            return null;
        }

        AnnonceDomain domain = new AnnonceDomain();
        domain.setId(entity.getId());
        domain.setDeparture(adresseToDomain(entity.getDeparture()));
        domain.setDestination(adresseToDomain(entity.getDestination()));
        domain.setPhotos(entity.getPhotos());
        domain.setDistance(entity.getDistance());
        domain.setPrix(entity.getPrix());
        domain.setAuthor(userToDomain(entity.getAuthor()));
        domain.setOpen(entity.isOpen());
        domain.setTitre(entity.getTitre());
        domain.setOffres(entity.getOffres());
        domain.setDate(entity.getDate());
        domain.setDeleted(entity.isDeleted());
        domain.setDeletedAt(entity.getDeletedAt());

        return domain;
    }

    public static UserEntity userToEntity(UserDomain domain) {
        if (domain == null) {
            return null;
        }

        UserEntity entity = new UserEntity();
        entity.setId(domain.getId());
        entity.setFirstname(domain.getFirstname());
        entity.setLastname(domain.getLastname());
        entity.setUsername(domain.getUsername());
        entity.setPassword(domain.getPassword());
        entity.setStatus(domain.getStatus());
        entity.setImage(domain.getImage());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setPhone(domain.getPhone());

        return entity;
    }

    public static UserDomain userToDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        UserDomain domain = new UserDomain();
        domain.setId(entity.getId());
        domain.setFirstname(entity.getFirstname());
        domain.setLastname(entity.getLastname());
        domain.setUsername(entity.getUsername());
        domain.setPassword(entity.getPassword());
        domain.setStatus(entity.getStatus());
        domain.setImage(entity.getImage());
        domain.setCreatedAt(entity.getCreatedAt());
        domain.setPhone(entity.getPhone());

        return domain;
    }

    public static OffreEntity offreToEntity(OffreDomain domain) {
        if (domain == null) {
            return null;
        }

        OffreEntity entity = new OffreEntity();
        entity.setId(domain.getId());
        entity.setPrix(domain.getPrix());
        entity.setAuthor(userToEntity(domain.getAuthor()));
        entity.setAnnonce(annonceToEntity(domain.getAnnonce()));
        entity.setDate(domain.getDate());
        entity.setDeleted(domain.isDeleted());
        entity.setDeletedAt(domain.getDeletedAt());

        return entity;
    }

    public static OffreDomain offreToDomain(OffreEntity entity) {
        if (entity == null) {
            return null;
        }

        OffreDomain domain = new OffreDomain();
        domain.setId(entity.getId());
        domain.setPrix(entity.getPrix());
        domain.setAuthor(userToDomain(entity.getAuthor()));
        domain.setAnnonce(annonceToDomain(entity.getAnnonce()));
        domain.setDate(entity.getDate());
        domain.setDeleted(entity.isDeleted());
        domain.setDeletedAt(entity.getDeletedAt());

        return domain;
    }

    public static UserTokenDomain userTokenToDomain(UserTokenEntity entity) {
        if (entity == null) {
            return null;
        }
        UserTokenDomain domain = new UserTokenDomain();
        domain.setId(entity.getId());
        domain.setRefreshToken(entity.getRefreshToken());
        domain.setUser(userToDomain(entity.getUser()));
        return domain;
    }

    public static UserTokenEntity userTokenEntity(UserTokenDomain domain) {
        if (domain == null) {
            return null;
        }
        UserTokenEntity entity = new UserTokenEntity();
        entity.setId(domain.getId());
        entity.setRefreshToken(domain.getRefreshToken());
        entity.setUser(userToEntity(domain.getUser()));
        return entity;
    }

}
