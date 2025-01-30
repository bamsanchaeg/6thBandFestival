package com.sixthband.festival.funding.dto;

import lombok.Data;

@Data
public class RequestKakaoPayDto {
    private String partner_order_id;
    private String partner_user_id;
    private String item_name;
    private int total_quantity;
    private int total_amount;
}
