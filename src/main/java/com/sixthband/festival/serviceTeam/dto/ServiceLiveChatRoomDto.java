package com.sixthband.festival.serviceTeam.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ServiceLiveChatRoomDto {
    public int id;
    public int user_id;
    public int service_id;
    public String is_Active;
    public Date created_at;
}
