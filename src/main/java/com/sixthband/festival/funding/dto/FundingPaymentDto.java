package com.sixthband.festival.funding.dto;

import java.util.Date;

import lombok.Data;

@Data
public class FundingPaymentDto {
    private int id;
    private int support_id;
    private String tid;
    private String cid;
    private Date created_at;
}
