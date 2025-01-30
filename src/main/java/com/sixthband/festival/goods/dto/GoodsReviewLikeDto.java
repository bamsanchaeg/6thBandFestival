package com.sixthband.festival.goods.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class GoodsReviewLikeDto {
    private int id;
    private int user_id;
    private int review_id;
    @DateTimeFormat(pattern = "yy-MM-dd")
    private LocalDate created_at;
}
