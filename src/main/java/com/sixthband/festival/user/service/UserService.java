package com.sixthband.festival.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sixthband.festival.dto.UserDto;
import com.sixthband.festival.user.mapper.UserSqlMapper;

@Service
public class UserService {

    @Autowired
    private UserSqlMapper userSqlMapper;

    public void userRegist(UserDto userDto){
        if(userDto.getPassword().equals("카카오")) {
            System.out.println("이거!!!"+userDto);
            userDto.setIsKaKao("Y");
            userSqlMapper.userRegist(userDto);
        }
        else{
            userSqlMapper.userRegist(userDto);

        }
    }
    //Restful API
    public boolean isExistAccountName(String account_name){
        return userSqlMapper.userExistId(account_name) > 0;
    }

    public UserDto loginUserExist(UserDto userDto){
        return userSqlMapper.loginUserRight(userDto);
    }

    // 마이페이지
    public UserDto getUserById(int id) {
        return userSqlMapper.userIdInfo(id);
    }

    // 카카오 간편 로그인 
    public UserDto kakaoLogin(String id) {
        // 해당 아이디가 있는지 확인
        boolean isExistKaKaoAccount = userSqlMapper.userExistId(id) > 0;

        UserDto kakaoUser = new UserDto();
        kakaoUser.setAccount_name(id);
        kakaoUser.setPassword("카카오");

        // 없으면 가입 처리
        if(!isExistKaKaoAccount) {
            kakaoUser.setCreated_at(null);
            userSqlMapper.userRegist(kakaoUser);
        }

        return kakaoUser;
    }

    public void updateUserInfo(UserDto userDto) {
        userSqlMapper.updateUserInfo(userDto);
    }
}
