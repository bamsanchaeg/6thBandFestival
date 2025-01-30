package com.sixthband.festival.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    // SessionNotFoundException 발생 시 처리하는 메서드
    @ExceptionHandler(SessionNotFoundException.class)
    public String handleSessionNotFoundException(SessionNotFoundException ex, HttpServletRequest request, Model model){
        // 예외 메시지를 모델에 추가 >> 해당 페이지
        model.addAttribute("errorMessage", ex.getMessage());

        // Referrer 확인
        String requestURI = request.getRequestURI();

        // 세션이 필요한 페이지에 따라 처리
        if (requestURI.startsWith("/admin")) {
            // 관리자
            model.addAttribute("loginUrl", "/admin/loginPage");

        } else if(requestURI.startsWith("/serviceTeam")) {
            // 서비스 팀
            model.addAttribute("loginUrl", "/serviceTeam");

        } else {
            // 일반 유저
            model.addAttribute("loginUrl", "/user/loginPage");
        }
        
        return "forward:/user/sessionNullPage";
    }

    // 다른 예외 처리도 가능 - Exception.class
}
