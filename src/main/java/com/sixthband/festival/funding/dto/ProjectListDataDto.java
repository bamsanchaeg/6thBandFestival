package com.sixthband.festival.funding.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ProjectListDataDto {
    private int id;
    private int category_id;
    private String project_title;
    private String thumbnail_location;
    private int expect_amount;
    private Date start_at;
    private Date end_at;
    private String status;
    private int read_count;
    private int rest_day;
    private int rest_hour;
    private int rest_minutes;
    private String creator_name;
    private String category_name;
    private int total_support_count;
    private int total_amount;
}
