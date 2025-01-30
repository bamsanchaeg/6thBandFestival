package com.sixthband.festival.club.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ClubBoardDto {

    private int id;
    private int club_member_id;
    private int club_category_id;
    private String title;
    private String content;
    private int club_id;
    private int read_count;
    private Date created_at;

}