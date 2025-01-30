package com.sixthband.festival.club.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ClubMemberRegistDto {

    private int id;
    private int user_id;
    private int club_id;
    private String regist_reason;
    private String self_introduction;
    private int member_activation;
    private String club_member_active;
    private Date created_at;

}
