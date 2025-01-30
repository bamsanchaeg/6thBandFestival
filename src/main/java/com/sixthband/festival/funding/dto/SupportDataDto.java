package com.sixthband.festival.funding.dto;

import java.util.Date;

import lombok.Data;

@Data
public class SupportDataDto {
    private int id;
    private int user_id;
    private String status;
    private String pay_status;
    private String delivery_status;
    private Date support_at;
    private Date Created_at;
    private int project_id;
    private String project_title;
    private String thumbnail_location;
    private Date end_at;
    private Date recipient_at;
}
