package com.sixthband.festival.club.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ClubPhotoLikeDto {
    private int id;
    private int club_photo_id;
    private int club_member_id;
    private Date created_at;
}