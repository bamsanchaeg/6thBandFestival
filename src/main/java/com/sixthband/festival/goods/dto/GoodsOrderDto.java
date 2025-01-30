package com.sixthband.festival.goods.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class GoodsOrderDto {
    private int id;
    private int user_id;
    private int goods_id;
    private int goods_order_count;
    private int order_price;
    private String receiver;
    private String address;
    private String phone;
    private String payment_info;
    private String request_details;
    private String order_status;
    @DateTimeFormat(pattern = "yy-MM-dd")
    private LocalDate created_at;

    private String user_name;
    
    private GoodsDto goodsDto; 

}
