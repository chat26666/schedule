package com.example.schedule.filter;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

public class LoginFilter implements Filter {

    static final String[] BlackListPath = {"/api/auth/login","/api/users"};

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestURI = httpRequest.getRequestURI();

        if((!isBlackList(requestURI) && httpRequest.getSession(false) != null) || isBlackList(requestURI))
            chain.doFilter(request,response);
        else httpResponse.sendError(511, "로그인이 필요합니다.");


    }
    public boolean isBlackList(String requestURI){

        return PatternMatchUtils.simpleMatch(BlackListPath, requestURI);
    }

}
