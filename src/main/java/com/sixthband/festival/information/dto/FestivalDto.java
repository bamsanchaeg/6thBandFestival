package com.sixthband.festival.information.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class FestivalDto {
    private int id;
    private String festival_name;
    private String festival_location;
    //'festival_introduction'
    private String festival_content;
    private String festival_reservationMethod;
    private String festival_caution;
    private String thumbnail;
    private String age_limit;
    //이 부분 포멧은 공연날짜 형식 이슈로 Datetime 설정
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date starting_date;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date end_date;
    private Timestamp created_at;


}
