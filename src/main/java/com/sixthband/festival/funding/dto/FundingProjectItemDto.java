package com.sixthband.festival.funding.dto;

import java.util.Date;

import lombok.Data;

@Data
public class FundingProjectItemDto {
    private int id;
    private int project_id;
    private String name;
    private String option_type;
    private Date created_at;
}
