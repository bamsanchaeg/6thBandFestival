package com.sixthband.festival.funding.dto;

import java.util.Date;

import lombok.Data;

@Data
public class FundingProjectReviewGptDto {
    private int id;
    private int review_id;
    private String gpt_content;
    private Date created_at;
}
