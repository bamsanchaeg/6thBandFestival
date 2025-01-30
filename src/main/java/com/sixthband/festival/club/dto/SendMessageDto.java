package com.sixthband.festival.club.dto;

import java.util.Date;

import lombok.Data;

@Data
public class SendMessageDto {
    private int id;
    private int sender_id;
    private int schedule_id;
    private String message;
    private Date created_at;
}
