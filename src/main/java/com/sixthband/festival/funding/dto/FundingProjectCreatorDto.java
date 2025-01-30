package com.sixthband.festival.funding.dto;

import java.util.Date;

import lombok.Data;

@Data
public class FundingProjectCreatorDto {
    private int id;
    private int user_id;
    private String creator_name;
    private String profile_location;
    private String introduce;
    private String bank_name;
    private String account_number;
    private String account_holder;
    private String account_birth;
    private Date created_at;
}
