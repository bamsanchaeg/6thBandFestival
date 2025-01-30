package com.sixthband.festival.funding.dto;

import java.util.Date;

import lombok.Data;

@Data
public class FundingProjectRewardItemDto {
    private int id;
    private int reward_id;
    private int item_id;
    private Date created_at;
}
