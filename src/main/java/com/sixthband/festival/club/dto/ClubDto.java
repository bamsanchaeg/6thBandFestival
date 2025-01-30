package com.sixthband.festival.club.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ClubDto {

    private int id;
    private int user_id;
    private String club_name;
    private String club_explanation;
    private int club_category_id;
    private int max_members;
    private int club_activation;
    private String club_logo;
    private String question1;
    private String question2;
    private String club_location;
    private Date created_at;

}