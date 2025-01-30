package com.sixthband.festival.funding.dto;

import java.util.Date;

import lombok.Data;

@Data
public class FundingProjectTagDto {
    private int id;
    private int project_id;
    private String tag;
    private Date created_at;
}
