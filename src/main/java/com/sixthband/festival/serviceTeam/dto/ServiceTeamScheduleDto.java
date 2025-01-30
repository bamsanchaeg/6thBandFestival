package com.sixthband.festival.serviceTeam.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Date;

@Data
public class ServiceTeamScheduleDto {
    public int id;
    public int service_id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date select_date;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date end_date;

//    Time 은 타입미스매치남!!
    public LocalTime working_time;
    public LocalTime quitting_time;
    public Date created_at;
}
