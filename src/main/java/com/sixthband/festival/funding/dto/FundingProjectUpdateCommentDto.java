package com.sixthband.festival.funding.dto;

import java.util.Date;

import lombok.Data;

@Data
public class FundingProjectUpdateCommentDto {
    private int id;
    private int update_id;
    private int user_id;
    private String content;
    private Date created_at;
}
