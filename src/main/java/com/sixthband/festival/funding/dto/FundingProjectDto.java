package com.sixthband.festival.funding.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class FundingProjectDto {
    private int id;
    private int user_id;
    private int category_id;
    private String project_title;
    private String project_desc;
    private String thumbnail_location;
    private String youtube_url;
    private int expect_amount;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date start_at;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date end_at;
    private String project_introduce;
    private String budget_desc;
    private String schedule_desc;
    private String team_desc;
    private String reward_desc;
    private String status;
    private int read_count;
    private Date created_at;
}
