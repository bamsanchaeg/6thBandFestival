package com.sixthband.festival.admin.service;

import com.sixthband.festival.admin.mapper.AdminSqlMapper;
import com.sixthband.festival.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdminService {

    @Autowired
    private AdminSqlMapper adminSqlMapper;

    public List<UserDto> getUserListAll(int page) {
        return adminSqlMapper.selectUserListAll(page);
    }

    public void updateUserIsActiveById(int id) {
        adminSqlMapper.updateUserIsActiveById(id);
    }

    public void updateUserIsActiveYById(int id) {
        adminSqlMapper.updateUserIsActiveYById(id);
    }

    public int userCount() {
        return adminSqlMapper.userCount();
    }

    // 대시보드용 회원목록
    public List<UserDto> getUserListForDashboard() {
        return adminSqlMapper.findUserListForDashboard();
    }
    
    // 활동 정지 회원 수
    public int getSuspendedUserCount() {
        return adminSqlMapper.findSuspendedUserCount();
    }

    // 대시보드용 매출 내역
    public List<Map<String, Object>> getWeeklySalesForDashboard() {
        return adminSqlMapper.findWeeklySalesForDashboard();
    }

    // 대시보드용 후기 내역
    public List<Map<String, Object>> getShopReviewListForDashboard() {
        return adminSqlMapper.findShopReviewListForDashboard();
    }
}
