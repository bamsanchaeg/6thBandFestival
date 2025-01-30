package com.sixthband.festival.club.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ClubMemberDto {
    private int id;
    private int club_member_id;
    private boolean club_chairman_right;
    private Date created_at;
}
