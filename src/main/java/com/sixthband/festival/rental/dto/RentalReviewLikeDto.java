package com.sixthband.festival.rental.dto;

import lombok.Data;

import java.util.Date;

@Data
public class RentalReviewLikeDto {
    private int id;
    private int review_id;
    private int user_id;
    private Date created_at;
}
