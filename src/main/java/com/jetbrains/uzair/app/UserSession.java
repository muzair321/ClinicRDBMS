package com.jetbrains.uzair.app;

import com.jetbrains.uzair.model.User;

public class UserSession {

    private static User currentUser;

    private UserSession() {}

    public static void login(User user) {
        currentUser = user;
    }

    public static void logout() {
        currentUser = null;
    }

    public static User getUser() {
        return currentUser;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static int getUserId() {
        if (currentUser == null)
            throw new IllegalStateException("No user logged in");
        return currentUser.getId();
    }

    public static boolean isAdmin() {
        return currentUser != null && currentUser.isAdmin();
    }
}