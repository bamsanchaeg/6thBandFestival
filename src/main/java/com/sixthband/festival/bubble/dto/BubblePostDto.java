package com.sixthband.festival.bubble.dto;


import lombok.Data;
import java.sql.Timestamp;

@Data
public class BubblePostDto {
    private int id;
    private int user_id;
    private String title;
    private String content;
    private Timestamp created_at;

}
