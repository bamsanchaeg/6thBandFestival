package com.sixthband.festival.serviceTeam.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ServiceTeamNoticeImageDto {
    public int id;
    public int notice_id;
    public String url;
    public Date created_at;
}
