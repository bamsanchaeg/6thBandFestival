package com.sixthband.festival.club.dto;

import lombok.Data;
import java.util.Date;

@Data
public class ChatGptReviewDto {
    private int id;
    private String Gpt_Review;
    private int comment_id;
    private Date created_at;
}
