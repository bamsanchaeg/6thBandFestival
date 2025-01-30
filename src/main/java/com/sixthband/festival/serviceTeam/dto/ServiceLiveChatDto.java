package com.sixthband.festival.serviceTeam.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ServiceLiveChatDto {
    public int id;
    public int room_id;
    public String from_email;
    public String message;
    public String is_reading;
    public Date created_at;
}
