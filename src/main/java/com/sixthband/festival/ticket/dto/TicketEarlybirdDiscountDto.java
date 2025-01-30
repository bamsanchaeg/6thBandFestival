package com.sixthband.festival.ticket.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TicketEarlybirdDiscountDto {
    private int id;
    private int info_id;
    private double discount;
    private int discount_quantity;
    private int remaining_quantity;
    private Date created_at;
}
