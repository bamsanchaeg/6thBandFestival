package com.sixthband.festival.bubble.dto;


import lombok.Data;

import java.sql.Timestamp;

@Data
public class BubbleCommentDto {
    private int id;
    private int user_id;
    private int post_id;
    private String content;
    private Timestamp created_at;
}
