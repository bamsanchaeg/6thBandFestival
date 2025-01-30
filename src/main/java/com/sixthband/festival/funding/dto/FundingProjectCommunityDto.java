package com.sixthband.festival.funding.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class FundingProjectCommunityDto {
    private int id;
    private int project_id;
    private int user_id;
    private String content;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date created_at;
}
