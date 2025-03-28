package com.example.schedule.util;
import jakarta.servlet.http.HttpSession;

public class SessionHelper {

    public static boolean isUserAuthorized(HttpSession session, Long userId) {
        Object sessionUserId = session.getAttribute("userId");
        return sessionUserId != null && sessionUserId.equals(userId);
    }
    public static Long getUserId(HttpSession session) {
        Object sessionUserId = session.getAttribute("userId");
        if (sessionUserId instanceof Long) {
            return (Long) sessionUserId;
        }
        return null;
    }
}