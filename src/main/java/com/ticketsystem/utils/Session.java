package com.ticketsystem.utils;

import com.ticketsystem.model.User;

public class Session {
    private static User currentUser;

    public static void setCurrentUser(User u) { currentUser = u; }
    public static User getCurrentUser() { return currentUser; }
}
