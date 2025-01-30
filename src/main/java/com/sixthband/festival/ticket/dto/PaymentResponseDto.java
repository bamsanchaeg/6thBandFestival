package com.sixthband.festival.ticket.dto;

import lombok.Data;

@Data
public class PaymentResponseDto {
    private String partner_order_id;       // 가맹점 주문번호
    private String partner_user_id;        // 가맹점 회원 id
    private String item_name;           // 상품 이름
    private int quantity;               // 상품 수량
    private int total_amount;           // 상품 총액
}
