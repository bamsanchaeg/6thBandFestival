package com.sixthband.festival.goods.dto;

import java.util.Date;

import lombok.Data;

@Data
public class GoodsGptReviewDto {
    private int id;
    private int comment_id;
    private String Gpt_Review;
    private Date created_at;
}
