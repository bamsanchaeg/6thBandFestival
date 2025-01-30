package com.sixthband.festival.user.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.sixthband.festival.dto.UserDto;

@Mapper
public interface UserSqlMapper {

    public void userRegist(UserDto userDto);

    public int userExistId(String account_name);

    public UserDto loginUserRight(UserDto userDto);
    
    //id값입력 유저정보 출력
    public UserDto userIdInfo(int id);

    public void updateUserInfo(UserDto userDto);

    
}

