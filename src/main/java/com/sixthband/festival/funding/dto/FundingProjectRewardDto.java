package com.sixthband.festival.funding.dto;

import java.util.Date;

import lombok.Data;

@Data
public class FundingProjectRewardDto {
    private int id;
    private int project_id;
    private String title;
    private int amount;
    private int count_limit;
    private int expect_date;
    private String is_delivery;
    private Date created_at;
}
