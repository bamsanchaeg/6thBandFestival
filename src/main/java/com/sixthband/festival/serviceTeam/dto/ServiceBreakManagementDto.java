package com.sixthband.festival.serviceTeam.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ServiceBreakManagementDto {
    public int id;
    public int service_id;
    public Date end_time;
    public Date created_at;
    public String is_done;
}
