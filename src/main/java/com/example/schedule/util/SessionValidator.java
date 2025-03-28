package com.example.schedule.util;
import jakarta.servlet.http.HttpSession;

public class SessionValidator {

    public static boolean isUserAuthorized(HttpSession session, Long userId) {
        Object sessionUserId = session.getAttribute("userId");
        return sessionUserId != null && sessionUserId.equals(userId);
    }
}