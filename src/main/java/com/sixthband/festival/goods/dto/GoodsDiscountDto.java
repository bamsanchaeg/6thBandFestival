package com.sixthband.festival.goods.dto;

import java.util.Date;

import lombok.Data;

@Data
public class GoodsDiscountDto {
    private int id;
    private int goods_id;
    private double discount;
    private Date created_at;

}
