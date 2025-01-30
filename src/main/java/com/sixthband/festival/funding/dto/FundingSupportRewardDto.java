package com.sixthband.festival.funding.dto;

import java.util.Date;

import lombok.Data;

@Data
public class FundingSupportRewardDto {
    private int id;
    private int support_id;
    private int reward_id;
    private int quantity;
    private Date created_at;
}
