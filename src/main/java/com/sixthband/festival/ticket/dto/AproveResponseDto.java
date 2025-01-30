package com.sixthband.festival.ticket.dto;

import lombok.Data;

import java.util.Date;

@Data
public class AproveResponseDto {
    private String aid;                 // 요청 고유 번호
    private String tid;                 // 결제 고유 번호
    private String cid;                 // 가맹점 코드
    private String partner_order_id;    // 가맹점 주문번호
    private String partner_user_id;     // 가맹점 회원 id
    private String payment_method_type; // 결제 수단, CARD 또는 MONEY 중 하나
    private AmountDto amount;        // 결제 금액 정보
//    private CardInfoDto cardInfo;    // 결제 상세 정보, 결제 수단이 카드일 경우만 포함
    private String item_name;           // 상품 이름
//    private String item_code;           // 상품 코드
    private int quantity;               // 상품 수량
    private Date created_at;           // 결제 준비 요청 시각
    private Date approved_at;          // 결제 승인 시각
//    private String payload;             // 결제 승인 요청에 대해 저장한 값, 요청 시 전달된 내용

}
