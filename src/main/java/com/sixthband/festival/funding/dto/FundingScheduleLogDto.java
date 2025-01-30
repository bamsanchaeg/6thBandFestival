package com.sixthband.festival.funding.dto;

import java.util.Date;

import lombok.Data;

@Data
public class FundingScheduleLogDto {
    private int id;
    private int project_id;
    private String old_status;
    private String new_status;
    private Date created_at;
}
