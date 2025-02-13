package com.startup.demenage.repository.Jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.startup.demenage.domain.AnnonceDomain;
import com.startup.demenage.repository.AnnonceRepository;
import com.startup.demenage.repository.Jpa.data.AnnonceEntity;
import com.startup.demenage.repository.Jpa.mappers.JpaMapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Repository
public class AnnonceRepositoryJpa implements AnnonceRepository {

    private static AnnonceRepositoryJpa instance;

    @PersistenceContext
    private EntityManager em;

    // private AnnonceRepositoryJpa() {
    // setUpJpa();
    // }

    @Override
    public Page<AnnonceDomain> findByDepartureCityContainingAndDestinationCityContaining(
            String cityDepart, String cityDestination, Pageable pageable) {
        String sql = "SELECT * FROM annonce where ville_depart LIKE :cityDepart AND ville_arrivee LIKE :cityDestination ;";
        Query query = em.createNativeQuery(sql, AnnonceEntity.class);
        query.setParameter("cityDepart", "%" + cityDepart + "%");
        query.setParameter("cityDestination", "%" + cityDestination + "%");
        List<AnnonceEntity> result = query.getResultList();

        int start = pageable.getPageNumber() * pageable.getPageSize();
        int end = Math.min(start + pageable.getPageSize(), result.size());
        List<AnnonceEntity> paginatedUsers = result.subList(start, end);

        return new PageImpl<AnnonceDomain>(paginatedUsers.stream().map(JpaMapper::annonceToDomain).toList(), pageable,
                result.size());
    }

    @Override
    public Page<AnnonceDomain> findByAuthor_Id(String author, Pageable pageable) {
        String sql = "SELECT * FROM annonce where author_id = :author ;";
        Query query = em.createNativeQuery(sql, AnnonceEntity.class);
        query.setParameter("author", author);

        List<AnnonceEntity> result = query.getResultList();
        int start = pageable.getPageNumber() * pageable.getPageSize();
        int end = Math.min(start + pageable.getPageSize(), result.size());
        List<AnnonceEntity> paginatedUsers = result.subList(start, end);

        return new PageImpl<AnnonceDomain>(paginatedUsers.stream().map(JpaMapper::annonceToDomain).toList(), pageable,
                result.size());
    }

    @Override
    public Optional<AnnonceDomain> findById(String id) {
        AnnonceEntity annonceEntity = em.find(AnnonceEntity.class, id);
        return annonceEntity != null ? Optional.of(JpaMapper.annonceToDomain(annonceEntity)) : Optional.empty();
    }

    @Override
    public AnnonceDomain save(AnnonceDomain annonceDomain) {
        AnnonceEntity entity = JpaMapper.annonceToEntity(annonceDomain);
        em.merge(entity);
        return annonceDomain;
    }

    @Override
    public void delete(AnnonceDomain aDomain) {
        AnnonceEntity annonceEntity = JpaMapper.annonceToEntity(aDomain);
        em.remove(annonceEntity);
    }

    // private void setUpJpa() {
    // EntityManagerFactory entityManagerFactory =
    // Persistence.createEntityManagerFactory("jpa");
    // EntityManager em = entityManagerFactory.createEntityManager();
    // this.em = em;
    // }

    public static AnnonceRepositoryJpa getInstance() {
        if (instance == null) {
            instance = new AnnonceRepositoryJpa();
        }
        return instance;
    }
}