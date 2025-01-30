package com.sixthband.festival.funding.dto;

import lombok.Data;

@Data
public class ProjectPledgeDto {
    private int reward_id;
    private int[] option_id;
    private String[] option_content;
    private int quantity;
}
