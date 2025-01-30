package com.sixthband.festival.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class UserDto {
    private int id;
    private String account_name;
    private String password;
    private String nickname;
    private String name;
    private String email;
    private String gender;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birth;
    private String phone;
    private String is_active;
    private String profile_img;
    private String isKaKao;
    private Date created_at;
}
