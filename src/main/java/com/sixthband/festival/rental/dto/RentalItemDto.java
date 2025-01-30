package com.sixthband.festival.rental.dto;

import java.util.Date;

import lombok.Data;

@Data
public class RentalItemDto {
    private int id;
    private int category_id;
    private String rental_title;
    private String title;
    private String content;
    private String image;
    private int stock_total;
    private String rental_location;
    private int price;
    private int deposit;
    private Date created_at;
}
