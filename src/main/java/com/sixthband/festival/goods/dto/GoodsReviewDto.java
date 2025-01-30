package com.sixthband.festival.goods.dto;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.sixthband.festival.dto.UserDto;

import lombok.Data;

@Data
public class GoodsReviewDto {
    private int id;
    private int order_id;
    private String content;
    private String image;
    private double rating;
    @DateTimeFormat(pattern = "yy-MM-dd")
    private LocalDate created_at;

    private GoodsDto goods;
    private UserDto user;
    private GoodsOrderDto order;

    private List<GoodsReviewDetailImageDto> reviewDetailImages;
    private List<GoodsReviewRecommendDto> reviewRecommends;
    private List<GoodsReviewLikeDto> reviewLikes;
}
