package com.sixthband.festival.funding.dto;

import java.util.Date;

import lombok.Data;

@Data
public class FundingProjectReviewDto {
    private int id;
    private int support_id;
    private String content;
    private Date created_at;
}
