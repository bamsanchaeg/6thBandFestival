package com.sixthband.festival.club.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class ClubVoteDto {
    private int id;
    private String question_title;
    private int club_member_id;
    private int club_id;
    @DateTimeFormat(pattern="yyyy-mm-dd")
    private Date final_date;
    private Date created_at;
}