package com.sixthband.festival.club.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ClubBoardCommentDto {
    private int id;
    private int club_board_id;
    private int club_member_id;
    private String content;
    private int club_id;
    private Date created_at;
}