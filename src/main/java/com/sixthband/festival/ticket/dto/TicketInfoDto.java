package com.sixthband.festival.ticket.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class TicketInfoDto {
    private int id;
    private int festival_id;
    private String day_type;
    private int adult_price;
    private int youth_price;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date open_date;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date deadline_date;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date booking_day;
    private int total_quantity;
    private Date created_at;
}
