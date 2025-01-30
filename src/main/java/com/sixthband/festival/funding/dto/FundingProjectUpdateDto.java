package com.sixthband.festival.funding.dto;

import java.util.Date;

import lombok.Data;

@Data
public class FundingProjectUpdateDto {
    private int id;
    private int project_id;
    private int user_id;
    private String content;
    private Date created_at;
}
