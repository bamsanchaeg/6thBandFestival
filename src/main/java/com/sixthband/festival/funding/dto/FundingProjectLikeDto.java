package com.sixthband.festival.funding.dto;

import java.util.Date;

import lombok.Data;

@Data
public class FundingProjectLikeDto {
    private int id;
    private int project_id;
    private int user_id;
    private Date created_at;
}
