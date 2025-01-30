package com.sixthband.festival.information.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class FestivalPerformanceDateDto {
 private int id;
 private int festival_id;
 @DateTimeFormat(pattern="yyyy-MM-dd")
 private Date performance_date;
 private Timestamp created_at;
}
