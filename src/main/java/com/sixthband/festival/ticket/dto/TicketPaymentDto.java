package com.sixthband.festival.ticket.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TicketPaymentDto {
    private int id;
    private int booking_id;
    private String tid;
    private String cid;
    private Date created_at;
}