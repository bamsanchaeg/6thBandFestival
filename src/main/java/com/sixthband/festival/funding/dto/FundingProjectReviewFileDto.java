package com.sixthband.festival.funding.dto;

import java.util.Date;

import lombok.Data;

@Data
public class FundingProjectReviewFileDto {
    private int id;
    private int review_id;
    private String location;
    private Date created_at;
}
