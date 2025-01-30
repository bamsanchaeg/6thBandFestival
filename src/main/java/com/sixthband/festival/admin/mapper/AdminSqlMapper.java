package com.sixthband.festival.admin.mapper;

import com.sixthband.festival.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.*;

@Mapper
public interface AdminSqlMapper {
//    유저 리스트
    public List<UserDto> selectUserListAll(@Param("page") int page);
//    유저 비활성화 (삭제 대신)
    public void updateUserIsActiveById(int id);
//    유저 활성화
    public void updateUserIsActiveYById(int id);

    public int userCount();

    // 유저 목록 : 대시보드 용
    public List<UserDto> findUserListForDashboard();
    public int findSuspendedUserCount(); // 활동정지 회원수
    // 매출 내역 : 대시보드용
    public List<Map<String, Object>> findWeeklySalesForDashboard();
    // 후기 취합 : 대시보드용
    public List<Map<String, Object>> findShopReviewListForDashboard();
}
