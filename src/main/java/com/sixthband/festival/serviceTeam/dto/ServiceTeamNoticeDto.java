package com.sixthband.festival.serviceTeam.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ServiceTeamNoticeDto {
    public int id;
    public int service_id;
    public String title;
    public String content;
    public int read_count;
    public String is_important;
    public Date created_at;
}
