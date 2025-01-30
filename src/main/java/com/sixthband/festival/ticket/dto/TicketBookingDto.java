package com.sixthband.festival.ticket.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TicketBookingDto {
    private int id;
    private int user_id;
    private int info_id;
    private int adult_quantity;
    private int youth_quantity;
    private int total_price;
    private String is_user;
    private Date created_at;
}
