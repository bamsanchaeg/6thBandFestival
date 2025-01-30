package com.sixthband.festival.funding.dto;

// import java.util.Date;

import lombok.Data;

@Data
public class ProjectPledgeStatusDto {
    private int project_id;
    private int expect_amount;
    private int fs_count;
    private int total_amount;
    private int percentage;
    // private Date end_at;
    // private int rest_day;
    // private int rest_hour;
}