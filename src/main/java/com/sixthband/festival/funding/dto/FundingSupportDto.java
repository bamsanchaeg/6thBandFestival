package com.sixthband.festival.funding.dto;

import java.util.Date;

import lombok.Data;

@Data
public class FundingSupportDto {
    private int id;
    private int user_id;
    // private String support_id;
    private String payment_type;
    private String status;
    private String pay_status;
    private String delivery_status;
    private Date support_at;
    private Date cancel_at;
    private Date delivery_at;
    private Date created_at;
}
