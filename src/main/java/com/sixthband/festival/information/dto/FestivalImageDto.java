package com.sixthband.festival.information.dto;


import lombok.Data;

import java.sql.Timestamp;

@Data
public class FestivalImageDto {
    private int id;
    private int festival_id;
    private String url;
    private Timestamp created_at;
}
