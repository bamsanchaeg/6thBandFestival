package com.sixthband.festival.serviceTeam.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ServiceQnaBoardDto {
    public int id;
    public int category_id;
    public int user_id;
    public int service_id;
    public String title;
    public String content;
    public String status;
    public Date created_at;
}
