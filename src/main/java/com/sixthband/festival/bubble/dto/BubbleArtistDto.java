package com.sixthband.festival.bubble.dto;


import lombok.Data;
import java.sql.Timestamp;

@Data
public class BubbleArtistDto {

    private int id;
    private int user_id;
    private String user_name;
    private String email_address;
    private String access_statement;
    private String identification_image;
    private Timestamp created_at;
}
