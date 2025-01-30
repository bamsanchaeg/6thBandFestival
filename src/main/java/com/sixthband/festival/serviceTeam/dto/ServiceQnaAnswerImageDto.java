package com.sixthband.festival.serviceTeam.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ServiceQnaAnswerImageDto {
    public int id;
    public int answer_id;
    public String url;
    public Date created_at;
}
