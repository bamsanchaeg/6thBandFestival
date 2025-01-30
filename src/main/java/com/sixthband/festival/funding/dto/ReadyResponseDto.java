package com.sixthband.festival.funding.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ReadyResponseDto {
    private String tid;                  // 결제 고유번호
    private String next_redirect_app_url;
    private String next_redirect_mobile_url; // 모바일 url
    private String next_redirect_pc_url; // 카카오톡으로 결제 요청 메시지(TMS)를 보내기 위한 사용자 정보 입력화면 Redirect URL (카카오 측 제공)
    private Date created_at;
}
