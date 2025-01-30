package com.sixthband.festival.club.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class ClubScheduleDto {
    private int id;
    private String schedule_title;
    private String schedule_explanation;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date start_date;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date end_date;
    private String schedule_location;
    private int max_members;
    private String schedule_image;
    private int club_id;
    private int club_member_id;
    private String weedo;
    private String gyungdo;
    private Date created_at;
}
