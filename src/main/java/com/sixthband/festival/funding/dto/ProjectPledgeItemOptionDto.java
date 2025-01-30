package com.sixthband.festival.funding.dto;

import lombok.Data;

@Data
public class ProjectPledgeItemOptionDto {
    private int id;
    private int support_reward_id;
    private int option_id;
    private String option_content;
    private String option_name;
    private int item_id;
}
