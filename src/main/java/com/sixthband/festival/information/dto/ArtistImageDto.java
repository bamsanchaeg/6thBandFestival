package com.sixthband.festival.information.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ArtistImageDto {

    private int id;
    private int artist_id;
    private String images_url;
    private Timestamp created_at;
}
