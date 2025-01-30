package com.sixthband.festival.user.controller;

import java.util.HashMap;
import java.util.Map;

import com.sixthband.festival.serviceTeam.dto.ServiceTeamDto;
import com.sixthband.festival.ticket.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.File;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sixthband.festival.dto.RestResponseDto;
import com.sixthband.festival.dto.UserDto;
import com.sixthband.festival.user.service.UserService;
import com.sixthband.festival.util.ImageUtil;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("six/user")
public class RestUserController {
    @Autowired
    private UserService userService;

    @Autowired
    private TicketService ticketService;

    @RequestMapping("isExistAccountName")
    public Map<String,Object> isExistAccountName(@RequestParam("accountName")String accountName){
        boolean userExist = userService.isExistAccountName(accountName);
        Map<String,Object> map = new HashMap<>();

        map.put("result",userExist);
        return map;

    }

    @RequestMapping("getSessionUserId")
    public RestResponseDto getSessionUserId(HttpSession session){
        RestResponseDto responseDto = new RestResponseDto();
        responseDto.setResult("success");

        UserDto sessionUser = (UserDto)session.getAttribute("loginUser");

        if(sessionUser != null){
            responseDto.add("id",sessionUser.getId());
        }
        else{
            responseDto.add("id",null);
        }

        return responseDto;

    }

    @RequestMapping("getMyBookingInfo")
    public RestResponseDto getMyBookingInfo(@RequestParam("id") int id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("bookingInfo", ticketService.getBookingById(id));
//        /six/user/getMyBookingInfo
        return restResponseDto;
    }

    @RequestMapping("userInfo")
    public RestResponseDto userInfo(@RequestParam("id") int id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("userInfo", userService.getUserById(id));
        return restResponseDto;
    }
    
    @RequestMapping("updateUserInfo")
    public RestResponseDto updateUserInfo(HttpSession session,UserDto userDto,@RequestParam(value = "uploadFiles", required = false)MultipartFile uploadFiles) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        UserDto sessionUser = (UserDto)session.getAttribute("loginUser");
        userDto.setId(sessionUser.getId());

        // 기존 이미지가 있으면 삭제

        if (uploadFiles != null && !uploadFiles.isEmpty()) {
            String existingImagePath = userDto.getProfile_img();
            String basePath = "/sixthBandImage/";

            if (existingImagePath != null && !existingImagePath.isEmpty()) {
                String fullPath = basePath + existingImagePath.replace("/", File.separator);
    
                File file = new File(fullPath);
    
                if (file.exists()) {
                    boolean isDeleted = file.delete(); 
                }
            }
    
            String image = ImageUtil.saveImageAndReturnLocation(uploadFiles);
            userDto.setProfile_img(image);
            userService.updateUserInfo(userDto);

        }

        else{
            userDto.setProfile_img(sessionUser.getProfile_img());
            userService.updateUserInfo(userDto);
        }
        return restResponseDto;
    }

    @RequestMapping("getAdminInfo")
    public RestResponseDto getAdminInfo(UserDto params) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("searchAdminInfo", userService.loginUserExist(params));

//        /api/service/getAdminInfo

        return restResponseDto;
    }

}