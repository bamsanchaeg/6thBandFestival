package com.sixthband.festival.funding.dto;

import java.util.Date;

import lombok.Data;

@Data
public class FundingProjectDescFileDto {
    private int id;
    private int project_id;
    private String location;
    private Date created_at;
}
