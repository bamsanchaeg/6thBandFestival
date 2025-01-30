package com.sixthband.festival.information.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ArtistDto {

    private int id;
    private String artist_name;
    private String artist_summary;
    private String artist_detail;
    private String thumbnail;
    private String youtube_url;
    private String sns_url;
    private Timestamp created_at;
}
