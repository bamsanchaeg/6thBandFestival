package com.sixthband.festival.rental.dto;

import java.util.Date;

import lombok.Data;

@Data
public class RentalChatGptReviewDto {
    private int id;
    private String Gpt_Review;
    private int comment_id;
    private Date created_at;
}
