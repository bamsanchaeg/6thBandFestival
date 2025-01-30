package com.sixthband.festival.club.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration  // Spring 설정 클래스임을 명시
@EnableWebSocket  // WebSocket 기능 활성화
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new ChatHandler(), "/chat")  // "/chat" 엔드포인트로 핸들러 등록
                .setAllowedOrigins("*");  // 모든 도메인에서의 연결 허용
    }
}
