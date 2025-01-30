package com.sixthband.festival.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sixthband.festival.dto.UserDto;
import com.sixthband.festival.user.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class Utils {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserService userService;

    //=========================================================================================
    //  Utils 사용법
    //  1. 사용할 Controller에 Autowired를 해주세요
    //  2. Autowired를 한 변수를 통해 필요한 기능을 불러와 사용합니다
    //=========================================================================================

    // 서버 URL 변수
    static public String FESTIVAL_URL = "https://festival.null-pointer-exception.com";

    // 특정 아이디로 로그인 합니다.
    public void loginUser(String account_name, String password) {
        UserDto params = new UserDto();
        params.setAccount_name(account_name);
        params.setPassword(password);
        UserDto userDto = userService.loginUserExist(params);
        request.getSession().setAttribute("loginUser", userDto);
    }

    // 로그아웃합니다.
    public void logOut() {
        request.getSession().invalidate();
    }

}
