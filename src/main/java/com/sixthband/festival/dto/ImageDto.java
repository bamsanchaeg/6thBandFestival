package com.sixthband.festival.dto;

import lombok.Data;

@Data
public class ImageDto {
    private int id;
    private int article_id;
    private String location;
    private String origin_filename;
}
