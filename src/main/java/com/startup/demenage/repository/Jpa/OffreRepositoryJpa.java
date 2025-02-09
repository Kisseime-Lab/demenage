package com.startup.demenage.repository.Jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.startup.demenage.domain.OffreDomain;
import com.startup.demenage.repository.OffreRepository;
import com.startup.demenage.repository.Jpa.data.OffreEntity;
import com.startup.demenage.repository.Jpa.mappers.JpaMapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Repository
public class OffreRepositoryJpa implements OffreRepository {

    private static OffreRepositoryJpa instance;

    public static OffreRepositoryJpa getInstance() {
        if (instance == null) {
            instance = new OffreRepositoryJpa();
        }
        return instance;
    }

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<OffreDomain> findByAnnonce_IdContainingAndAuthor_IdContaining(String annonceId, String authorId,
            Pageable pageable) {
        String sql = "SELECT * FROM offre WHERE id LIKE :annonceId AND author_id LIKE :authordId ; ";
        Query query = em.createNativeQuery(sql, OffreEntity.class);

        query.setParameter("annonceId", "%" + annonceId + "%");
        query.setParameter("authorId", "%" + authorId + "%");

        List<OffreEntity> result = query.getResultList();

        return new PageImpl<OffreDomain>(result.stream().map(JpaMapper::offreToDomain).toList());
    }

    @Override
    public Page<OffreDomain> findByAuthor_Id(String author, Pageable pageable) {
        String sql = "SELECT * FROM offre WHERE author_id = :authordId ; ";
        Query query = em.createNativeQuery(sql, OffreEntity.class);

        query.setParameter("authorId", author);

        List<OffreEntity> result = query.getResultList();

        return new PageImpl<OffreDomain>(result.stream().map(JpaMapper::offreToDomain).toList());

    }

    @Override
    public OffreDomain save(OffreDomain offreDomain) {
        OffreEntity entity = JpaMapper.offreToEntity(offreDomain);
        em.persist(entity);
        return offreDomain;
    }

    @Override
    public Optional<OffreDomain> findById(String id) {
        String sql = "SELECT * FROM offre WHERE id = :id ; ";
        Query query = em.createNativeQuery(sql, OffreEntity.class);

        query.setParameter("id", id);

        List<OffreEntity> result = query.getResultList();

        return result.size() > 0 ? Optional.of(JpaMapper.offreToDomain(result.get(0))) : Optional.empty();
    }

    @Override
    public void delete(OffreDomain offre) {
        OffreEntity offreEntity = JpaMapper.offreToEntity(offre);
        em.remove(offreEntity);
    }

}
