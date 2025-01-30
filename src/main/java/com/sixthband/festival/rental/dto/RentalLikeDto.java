package com.sixthband.festival.rental.dto;

import java.util.Date;

import lombok.Data;

@Data
public class RentalLikeDto {
    private int id;
    private int item_id;
    private int user_id;
    private Date created_at;
}
