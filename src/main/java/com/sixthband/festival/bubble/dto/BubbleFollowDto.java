package com.sixthband.festival.bubble.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class BubbleFollowDto {
    private int id;
    private int following_id;
    private int follower_id;
    private Timestamp created_at;

}
