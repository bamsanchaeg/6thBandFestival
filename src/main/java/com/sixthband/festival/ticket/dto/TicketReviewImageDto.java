package com.sixthband.festival.ticket.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TicketReviewImageDto {
    private int id;
    private int review_id;
    private String url;
    private Date created_at;
}
