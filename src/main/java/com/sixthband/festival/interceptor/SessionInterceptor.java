package com.sixthband.festival.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.sixthband.festival.exception.SessionNotFoundException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class SessionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // System.out.println("[SessionInterceptor] ::: preHandle 실행됨");
        
        HttpSession session = request.getSession();
        String requestURI = request.getRequestURI();

        if(requestURI.startsWith("/admin")) {
            // 1. /admin 경로
            if(session.getAttribute("admin") == null) {
                throw new SessionNotFoundException("[SessionInterceptor] ::: 관리자 세션이 필요합니다.");
            }

        } else if(requestURI.startsWith("/serviceTeam")) {
            // 2. /serviceTeam 경로
            if(session.getAttribute("loginServiceTeam") == null) {
                throw new SessionNotFoundException("[SessionInterceptor] ::: 서비스 팀 세션이 필요합니다.");
            }

        } else {
            if(session.getAttribute("loginUser") == null) {
                throw new SessionNotFoundException("[SessionInterceptor] ::: 사용자 세션이 필요합니다.");
            }

        }

        return true; // 세션이 유효할 경우 계속 진행
    }
}
