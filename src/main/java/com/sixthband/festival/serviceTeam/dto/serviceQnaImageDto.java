package com.sixthband.festival.serviceTeam.dto;

import lombok.Data;

import java.util.Date;

@Data
public class serviceQnaImageDto {
    public int id;
    public int board_id;
    public String url;
    public Date created_at;
}
