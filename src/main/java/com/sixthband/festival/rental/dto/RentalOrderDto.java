package com.sixthband.festival.rental.dto;

import java.util.Date;

import lombok.Data;

@Data
public class RentalOrderDto {
    private int id;
    private int user_id;
    private int rental_item_id;
    private String receiver;
    private int count;
    private String payment_method;
    private String rental_order_status;
    private int rental_days;
    private int total_price;
    private Date created_at;
}
