package com.example.schedule.filter;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class LoginFilter implements Filter {

    private final ObjectMapper objectMapper = new ObjectMapper();
    static final String[] BlackListPath = {"/api/auth/login","/api/users"};

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestURI = httpRequest.getRequestURI();

        if((!isBlackList(requestURI) && httpRequest.getSession(false) != null) || isBlackList(requestURI))
            chain.doFilter(request,response);
        else {
            httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());  // 400
            httpResponse.setContentType("application/json;charset=UTF-8");

            Map<String, Object> errorBody = new LinkedHashMap<>();
            String formattedTimestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new Date());
            errorBody.put("timestamp", formattedTimestamp);
            errorBody.put("status", HttpStatus.UNAUTHORIZED.value());
            errorBody.put("error", "UNAUTHORIZED");
            errorBody.put("message", "로그인이 필요합니다");
            errorBody.put("path", httpRequest.getRequestURI());
            String errorJson = objectMapper.writeValueAsString(errorBody);
            response.getWriter().write(errorJson);
        }


    }
    public boolean isBlackList(String requestURI){

        return PatternMatchUtils.simpleMatch(BlackListPath, requestURI);
    }

}
