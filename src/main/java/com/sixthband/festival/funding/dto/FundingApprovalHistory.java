package com.sixthband.festival.funding.dto;

import java.util.Date;

import lombok.Data;

@Data
public class FundingApprovalHistory {
    private int id;
    private int project_id;
    private String result;
    private String content;
    private Date created_at;
}
