package com.sixthband.festival.serviceTeam.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ServiceQnaAnswerDto {
    public int id;
    public int board_id;
    public String content;
    public Date created_at;
}
