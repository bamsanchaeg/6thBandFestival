package com.sixthband.festival.club.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ClubVoteOptionDto {
    private int id;
    private int vote_id;
    private String question_content;
    private String question_image;
    private int club_id;
    private int club_member_id;
    private Date created_at;

}