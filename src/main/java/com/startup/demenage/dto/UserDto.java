package com.startup.demenage.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.startup.demenage.domain.UserDomain;
import com.startup.demenage.model.User;

@Component
public class UserDto {

    public User toModel(UserDomain userDomain) {
        if (userDomain == null) {
            return null;
        }
        User user = new User();
        BeanUtils.copyProperties(userDomain, user);
        user.setRole(userDomain.getRole().name());
        user.setPassword(null);
        return user;
    }

    public List<User> toListModel(List<UserDomain> userEntityList) {
        List<User> users = new ArrayList<>();
        return userEntityList.stream().map(this::toModel).toList();
    }
}
