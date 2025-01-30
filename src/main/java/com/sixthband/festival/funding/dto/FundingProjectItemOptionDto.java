package com.sixthband.festival.funding.dto;

import java.util.Date;

import lombok.Data;

@Data
public class FundingProjectItemOptionDto {
    private int id;
    private int item_id;
    private String option_name;
    private Date created_at;
}
