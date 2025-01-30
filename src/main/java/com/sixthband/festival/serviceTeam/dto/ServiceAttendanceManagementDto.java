package com.sixthband.festival.serviceTeam.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ServiceAttendanceManagementDto {
    public int id;
    public int service_id;
//    public Date work_time;
    public String status;
    public Date created_at;
}
