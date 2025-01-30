package com.sixthband.festival.goods.dto;

import java.util.Date;

import lombok.Data;

@Data
public class GoodsReviewRecommendDto {
    private int id;
    private int goods_review_id;
    private String recommend;
    private Date created_at;
}
