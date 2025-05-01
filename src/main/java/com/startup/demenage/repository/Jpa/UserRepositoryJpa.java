package com.startup.demenage.repository.jpa;

import java.util.List;
import java.util.Optional;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.startup.demenage.domain.UserDomain;
import com.startup.demenage.repository.UserRepository;
import com.startup.demenage.repository.StubMemory.StubMemoryUserRepository;
import com.startup.demenage.repository.jpa.data.UserEntity;
import com.startup.demenage.repository.jpa.mappers.JpaMapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Repository
@Transactional
public class UserRepositoryJpa implements UserRepository {

    private static UserRepositoryJpa instance;
    private static final Logger logger = LoggerFactory.getLogger(UserRepositoryJpa.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<UserDomain> findByUsername(String username) {
        logger.info("start findByUsername method");
        String sql = "SELECT * FROM author where username = :username ;";
        Query query = em.createNativeQuery(sql, UserEntity.class);
        query.setParameter("username", username);

        List<UserEntity> result = query.getResultList();

        return result.size() > 0 ? Optional.of(JpaMapper.userToDomain(result.get(0))) : Optional.empty();
    }

    @Override
    public UserDomain save(UserDomain user) {
        UserEntity userEntity = JpaMapper.userToEntity(user);
        em.persist(userEntity);
        return user;
    }

    @Override
    public void delete(UserDomain user) {
        UserEntity userEntity = em.find(UserEntity.class, user.getId());
        Hibernate.initialize(userEntity.getToken());
        if (userEntity != null) {
            em.remove(userEntity); // Supprime l'entité attachée
        }
        logger.info("user deleted successfully");
    }

    @Override
    public void deleteByUserId(String id) {
        String sql = "DELETE FROM author where id = :id ;";
        Query query = em.createNativeQuery(sql, UserEntity.class);
        query.setParameter("id", id);

        query.executeUpdate();
    }

    @Override
    public Optional<UserDomain> findById(String id) {
        String sql = "SELECT * FROM author where id = :id ;";
        Query query = em.createNativeQuery(sql, UserEntity.class);
        query.setParameter("id", id);

        List<UserEntity> result = query.getResultList();
        return result.size() > 0 ? Optional.of(JpaMapper.userToDomain(result.get(0))) : Optional.empty();
    }
}
