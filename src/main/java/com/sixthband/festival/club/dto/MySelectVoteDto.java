package com.sixthband.festival.club.dto;

import java.util.Date;

import lombok.Data;

@Data
public class MySelectVoteDto {
    private int id;
    private int option_id;
    private int vote_id;
    private int club_member_id;
    private int club_id;
    private Date created_at;
}
