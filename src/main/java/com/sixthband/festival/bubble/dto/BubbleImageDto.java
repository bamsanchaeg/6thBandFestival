package com.sixthband.festival.bubble.dto;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class BubbleImageDto {
    private int id;
    private int post_id;
    private String image_url;
    private Timestamp created_at;
}
