package com.startup.demenage.repository.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.startup.demenage.domain.UserTokenDomain;
import com.startup.demenage.repository.UserTokenRepository;
import com.startup.demenage.repository.jpa.data.UserEntity;
import com.startup.demenage.repository.jpa.data.UserTokenEntity;
import com.startup.demenage.repository.jpa.mappers.JpaMapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Repository
public class UserTokenRepositoryJpa implements UserTokenRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<UserTokenDomain> findByRefreshToken(String refreshToken) {
        String sql = "SELECT * FROM user_token WHERE refresh_token = :token ; ";
        Query query = em.createNativeQuery(sql, UserTokenEntity.class);

        query.setParameter("token", refreshToken);

        List<UserTokenEntity> result = query.getResultList();

        return result.isEmpty() ? Optional.of(JpaMapper.userTokenToDomain(result.get(0))) : Optional.empty();
    }

    @Override
    public void deleteByUserId(String userId) {
        String sql = "DELETE FROM user_token WHERE user_id = :id ; ";
        Query query = em.createNativeQuery(sql, UserTokenEntity.class);

        query.setParameter("id", userId);

        query.executeUpdate();
    }

    @Override
    public void save(UserTokenDomain userTokenDomain) {
        UserTokenEntity userTokenEntity = JpaMapper.userTokenEntity(userTokenDomain);
        UserEntity user = em.find(UserEntity.class,
                userTokenEntity.getUser().getId());
        user.getToken().add(userTokenEntity);
        userTokenEntity.setUser(user);
        em.merge(user);
    }

    @Override
    public void delete(UserTokenDomain userTokenDomain) {
        UserTokenEntity entity = JpaMapper.userTokenEntity(userTokenDomain);
        em.remove(entity);
    }

}
