package com.sixthband.festival.funding.dto;

import java.util.Date;

import lombok.Data;

@Data
public class FundingSettlementDetailsDto {
    private int id;
    private int project_id;
    private int settlement_amount;
    private int settlement_fee;
    private String settlement_status;
    private Date settlement_date;
    private Date created_at;
}
