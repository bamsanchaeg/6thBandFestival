package com.sixthband.festival.club.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ClubPhotoDto {
    private int id;
    private int club_member_id;
    private String club_photo_img;
    private int club_id;
    private Date created_at;
}