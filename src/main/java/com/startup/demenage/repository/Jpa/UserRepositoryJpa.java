package com.startup.demenage.repository.Jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.startup.demenage.domain.UserDomain;
import com.startup.demenage.repository.UserRepository;
import com.startup.demenage.repository.Jpa.data.UserEntity;
import com.startup.demenage.repository.Jpa.mappers.JpaMapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Repository
public class UserRepositoryJpa implements UserRepository {

    private static UserRepositoryJpa instance;

    public static UserRepositoryJpa getInstance() {
        if (instance == null) {
            instance = new UserRepositoryJpa();
        }
        return instance;
    }

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<UserDomain> findByUsername(String username) {
        String sql = "SELECT * FROM demenage.author where username = :username ;";
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
        UserEntity userEntity = JpaMapper.userToEntity(user);
        em.remove(userEntity);
    }

    @Override
    public void deleteByUserId(String id) {
        String sql = "DELETE FROM demenage.author where id = :id ;";
        Query query = em.createNativeQuery(sql, UserEntity.class);
        query.setParameter("id", id);

        query.executeUpdate();
    }

    @Override
    public Optional<UserDomain> findById(String id) {
        String sql = "SELECT * FROM demenage.author where id = :id ;";
        Query query = em.createNativeQuery(sql, UserEntity.class);
        query.setParameter("id", id);

        List<UserEntity> result = query.getResultList();
        return result.size() > 0 ? Optional.of(JpaMapper.userToDomain(result.get(0))) : Optional.empty();
    }
}
