package com.sixthband.festival.serviceTeam.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class ServiceTeamDto {
    private int id;
    private String email;
    private String password;
    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birth;

    private String gender;
    private String phone;
    private String profile;
    private String position;
    private Date created_at;
}
