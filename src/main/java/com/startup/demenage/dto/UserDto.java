package com.startup.demenage.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.startup.demenage.entity.UserEntity;
import com.startup.demenage.model.User;

@Component
public class UserDto {

    public User toModel(UserEntity userEntity) {
        User user = new User();
        BeanUtils.copyProperties(userEntity, user);
        user.setRole(userEntity.getRole().name());
        user.setPassword(null);
        return user;
    }

    public List<User> toListModel(List<UserEntity> userEntityList) {
        List<User> users = new ArrayList<>();
        return userEntityList.stream().map(this::toModel).toList();
    }
}
