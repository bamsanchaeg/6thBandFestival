package com.sixthband.festival.information.dto;


import lombok.Data;

import java.sql.Timestamp;

@Data
public class FestivalLikeDto {
    private int id;
    private int festival_id;
    private int user_id;
    private Timestamp created_at;
}
