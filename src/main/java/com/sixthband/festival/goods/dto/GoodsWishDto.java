package com.sixthband.festival.goods.dto;

import java.util.Date;

import lombok.Data;

@Data
public class GoodsWishDto {
    private int id;
    private int goods_id;
    private int user_id;
    private Date created_at;
}
