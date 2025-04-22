package com.startup.demenage.controller;

import com.startup.demenage.model.SignInReq;
import com.startup.demenage.model.User;

public class TestUtils {

    public static String username = "ktevot@gmail.com";
    public static String password = "Test1234#";

    public static User createUser() {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setFirstname("lorem");
        user.setLastname("Ipsum");
        user.setPhone("0758828728");
        return user;
    }

    public static SignInReq createSignInReq() {
        return new SignInReq()
                .username(username)
                .password(password);
    }
}
