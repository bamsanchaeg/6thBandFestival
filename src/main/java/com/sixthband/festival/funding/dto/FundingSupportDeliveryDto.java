package com.sixthband.festival.funding.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class FundingSupportDeliveryDto {
    private int id;
    private int support_id;
    // private String status;
    private String zip_code;
    private String address;
    private String address_detail;
    private String recipient_name;
    private String recipient_phone;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date recipient_at;
}
