package com.sixthband.festival.ticket.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TicketReviewDto {
    private int id;
    private int user_id;
    private int booking_id;
    private String title;
    private String content;
    private Date created_at;
}
