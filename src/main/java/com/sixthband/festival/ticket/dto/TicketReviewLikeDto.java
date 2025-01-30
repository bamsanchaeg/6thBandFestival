package com.sixthband.festival.ticket.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TicketReviewLikeDto {
    private int id;
    private int user_id;
    private int review_id;
    private Date created_at;
}
