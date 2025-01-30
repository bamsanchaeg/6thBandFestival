package com.sixthband.festival.goods.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class GoodsDto {
    private int id;
    private int category_id;
    private String goods_title;
    private String image;
    private int goods_count;
    private int price;
    private String detail;
    private Date created_at;
    private Integer discount; 

    private List<GoodsDetailImageDto> goodsDetailImageDtoList;
    private GoodsDetailImageDto goodsDetailImageDto;
    private List<GoodsOrderDto> goodsOrderDtoList;
    private GoodsOrderDto goodsOrderDto;
    private GoodsCategoryDto goodsCategoryDto;
}
