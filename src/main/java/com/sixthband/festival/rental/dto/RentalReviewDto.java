package com.sixthband.festival.rental.dto;



import java.util.Date;

import lombok.Data;

@Data
public class RentalReviewDto {
    private int id;
    private int user_id;
    private int order_id;
    private String content;
    private String image;
    private String item_condition;
    private double rating;
    private Date created_at;
}
