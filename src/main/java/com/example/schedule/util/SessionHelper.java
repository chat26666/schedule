package com.example.schedule.util;
import jakarta.servlet.http.HttpSession;

public class SessionHelper {

    public static Long getUserId(HttpSession session) {
        Object sessionUserId = session.getAttribute("userId");
        return (Long) sessionUserId;
    }
}