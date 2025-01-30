package com.sixthband.festival.admin.controller;

import com.sixthband.festival.admin.service.AdminService;
import com.sixthband.festival.dto.RestResponseDto;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/admin")
public class RestAdminController {

    @Autowired
    private AdminService adminService;

    @RequestMapping("userList")
    public RestResponseDto userList(@RequestParam(value = "page", defaultValue = "1") int page) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("userList", adminService.getUserListAll((page - 1) * 15));
        restResponseDto.add("userCount", adminService.userCount());

//      /api/admin/userList
        return restResponseDto;
    }

    @RequestMapping("disableUserById")
    public RestResponseDto disableUserById(@RequestParam("id") int id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        adminService.updateUserIsActiveById(id);

//      /api/admin/disabledUserById
        return restResponseDto;
    }

    @RequestMapping("ableUserById")
    public RestResponseDto ableUserById(@RequestParam("id") int id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        adminService.updateUserIsActiveYById(id);

//      /api/admin/ableUserById
        return restResponseDto;
    }

    // 대시보드용 : 주간 매출 현황
    @RequestMapping("weeklySales")
    public RestResponseDto weeklySales() {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("seccess");

        List<Map<String, Object>> weeklySales = adminService.getWeeklySalesForDashboard();
        restResponseDto.add("weeklySales", weeklySales);

        return restResponseDto;
    }
}
