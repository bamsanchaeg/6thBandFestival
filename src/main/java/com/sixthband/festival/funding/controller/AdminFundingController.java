package com.sixthband.festival.funding.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import com.sixthband.festival.funding.dto.FundingProjectCategoryDto;
import com.sixthband.festival.funding.dto.FundingProjectCreatorDto;
import com.sixthband.festival.funding.dto.FundingSettlementDetailsDto;
import com.sixthband.festival.funding.dto.FundingSupportDto;
import com.sixthband.festival.funding.service.FundingService;
import com.sixthband.festival.user.service.UserService;

@Controller
@RequestMapping("admin/funding")
public class AdminFundingController {

    @Autowired
    private FundingService fundingService;

    @Autowired
    private UserService userService;

    // 프로젝트 관리 : 목록
    @RequestMapping("projectList")
    public String projectList(
        Model model, @RequestParam(value="page", defaultValue="1") int pageIndex,
        @RequestParam(value="categoryId", required=false) Integer categoryId,
        @RequestParam(value="fromStartDate", required=false) String fromStartDate,
        @RequestParam(value="toStartDate", required=false) String toStartDate,
        @RequestParam(value="status", required=false) String[] statusList,
        @RequestParam(value="fromEndDate", required=false) String fromEndDate,
        @RequestParam(value="toEndDate", required=false) String toEndDate,
        @RequestParam(value="searchOption", required=false) String searchOption,
        @RequestParam(value="searchWord", required=false) String searchWord
    ) { 
        
        List<Map<String, Object>> projectDataList = fundingService.getAdminProjectList(
            pageIndex, categoryId, fromStartDate, toStartDate, statusList, fromEndDate, toEndDate, searchOption, searchWord
        );
        model.addAttribute("projectDataList", projectDataList);

        int projectListCount = fundingService.getAdminProjectListCount(
            categoryId, fromStartDate, toStartDate, statusList, fromEndDate, toEndDate, searchOption, searchWord
        );
        model.addAttribute("projectListCount", projectListCount);

        // 카테고리
        List<FundingProjectCategoryDto> projectCategoryList = fundingService.getProjectCategoryList();
        model.addAttribute("projectCategoryList", projectCategoryList);

        // 페이징 처리
        int lastPageNumber = (int)Math.ceil(projectListCount/10.0);
        model.addAttribute("lastPageNumber", lastPageNumber);
        model.addAttribute("currentPage", pageIndex);

        int startPage = ((pageIndex-1)/5)*5 + 1; // 시작점
        int endPage = ((pageIndex-1)/5+1)*5; // 끝
        if(endPage > lastPageNumber) {
            endPage = lastPageNumber;
        }
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        // prams
        List<String> paramList = new ArrayList<>();
        if(categoryId != null && categoryId != 0) { paramList.add("categoryId="+categoryId); }
        if(fromStartDate != null && fromStartDate != "") { paramList.add("fromStartDate="+fromStartDate); }
        if(toStartDate != null && toStartDate != "") { paramList.add("toStartDate="+toStartDate); }
        if(statusList != null && statusList.length > 0) {
            String tempStatus = "";
            for(int i=0; i<statusList.length; i++) {
                if(i > 0) {
                    tempStatus += "&";
                }
                tempStatus += "status="+statusList[i];
            }
            paramList.add(tempStatus); 
        }
        if(fromEndDate != null && fromEndDate != "") { paramList.add("fromEndDate="+fromEndDate); }
        if(toEndDate != null && toEndDate != "") { paramList.add("toEndDate="+toEndDate); }
        if(searchOption != null && searchOption != "") { paramList.add("searchOption="+searchOption); }
        if(searchWord != null && searchWord != "") { paramList.add("searchWord="+searchWord); }
        
        String searchParam = "";
        if(!paramList.isEmpty()) {
            searchParam = String.join("&", paramList);
            searchParam = "&" + searchParam;
            // searchParam = "?" + searchParam;
        }
        model.addAttribute("searchParam", searchParam);

        return "admin/funding/projectList";
    }

    // 프로젝트 관리 : 상세
    @RequestMapping("projectDetail")
    public String projectDetail(@RequestParam("id") int id, Model model) {

        // 프로젝트 정보
        Map<String, Object> projectData = fundingService.getProjectData(id);
        // 개행 처리..
        Map<String, Object> fundingProjectDto = (Map<String, Object>)projectData.get("project");
        String escapeIntroduce = StringUtils.escapeXml(fundingProjectDto.get("project_introduce"));
        escapeIntroduce = escapeIntroduce.replace("\n", "<br>");
        fundingProjectDto.put("project_introduce", escapeIntroduce);
        String escapeBudgetDesc = StringUtils.escapeXml(fundingProjectDto.get("budget_desc"));
        escapeBudgetDesc = escapeBudgetDesc.replace("\n", "<br>");
        fundingProjectDto.put("budget_desc", escapeBudgetDesc);
        String escapeScheduleDesc = StringUtils.escapeXml(fundingProjectDto.get("schedule_desc"));
        escapeScheduleDesc = escapeScheduleDesc.replace("\n", "<br>");
        fundingProjectDto.put("schedule_desc", escapeScheduleDesc);
        String escapeTeamDesc = StringUtils.escapeXml(fundingProjectDto.get("team_desc"));
        escapeTeamDesc = escapeTeamDesc.replace("\n", "<br>");
        fundingProjectDto.put("team_desc", escapeTeamDesc);
        String escapeRewardDesc = StringUtils.escapeXml(fundingProjectDto.get("reward_desc"));
        escapeRewardDesc = escapeRewardDesc.replace("\n", "<br>");
        fundingProjectDto.put("reward_desc", escapeRewardDesc);
        model.addAttribute("projectData", projectData);

        // 프로젝트 상품- 선물 정보
        List<Map<String, Object>> rewardDataList = fundingService.getProjectRewardDataList(id);
        model.addAttribute("rewardDataList", rewardDataList);
        // 프로젝트 상품- 아이템 정보
        List<Map<String, Object>> itemDataList = fundingService.getProjectItemDataList(id);
        model.addAttribute("itemDataList", itemDataList);

        // 첨부파일
        Map<String, Object> projectFile = fundingService.getProjectDescFile(id);
        model.addAttribute("projectFile", projectFile);
        
        return "admin/funding/projectDetail";
    }

    // 후원자 후원 관리 : 목록
    @RequestMapping("backersList")
    public String backersList(Model model, @RequestParam(value="page", defaultValue="1") int pageIndex) {
        List<Map<String, Object>> backersDataList = fundingService.getAdminBackersList(pageIndex);
        model.addAttribute("backersDataList", backersDataList);

        int backersListCount = fundingService.getAdminBackersListCount();
        model.addAttribute("backersListCount", backersListCount);

        // 페이징 처리
        int lastPageNumber = (int)Math.ceil(backersListCount/10.0);
        model.addAttribute("lastPageNumber", lastPageNumber);
        model.addAttribute("currentPage", pageIndex);

        int startPage = ((pageIndex-1)/5)*5 + 1; // 시작점
        int endPage = ((pageIndex-1)/5+1)*5; // 끝
        if(endPage > lastPageNumber) {
            endPage = lastPageNumber;
        }
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "admin/funding/backersList";
    }

    // 후원자 후원 관리 : 상세
    @RequestMapping("backersDetail")
    public String backersDetail(@RequestParam("id") int id, Model model) {

        // 프로젝트+창작자 정보
        Map<String, Object> projectData = fundingService.getProjectDataForPledgeBySupportId(id);
        model.addAttribute("projectData", projectData);

        // 선택한 선물, 아이템 정보 : 프로젝트 단일
        List<Map<String, Object>> projectRewardDataList = fundingService.getSupportProjectRewardDataList(id);
        model.addAttribute("projectRewardDataList", projectRewardDataList);
        // 전체 금액
        int supportTotalPirce = fundingService.getSupportTotalPrice(id);
        model.addAttribute("supportTotalPirce", supportTotalPirce);

        // 후원 정보
        Map<String, Object> supportData = fundingService.getSupportData(id);
        model.addAttribute("supportData", supportData);

        // 후원자 정보(회원)
        FundingSupportDto supportDto = (FundingSupportDto)supportData.get("supportDto"); 
        int userId = supportDto.getUser_id();
        model.addAttribute("userInfo", userService.getUserById(userId));

        return "admin/funding/backersDetail";
    }

    // 정산 관리 : 목록
    @RequestMapping("settlementList")
    public String settlementList(Model model, @RequestParam(value="page", defaultValue="1") int pageIndex) {

        List<Map<String, Object>> settlementDetailDataList = fundingService.getSettlementDetailsList(pageIndex);
        model.addAttribute("settlementDetailDataList", settlementDetailDataList);

        int settlementListCount = fundingService.getSettlementDetailsListCount();
        model.addAttribute("settlementListCount", settlementListCount);

        // 페이징 처리
        int lastPageNumber = (int)Math.ceil(settlementListCount/10.0);
        model.addAttribute("lastPageNumber", lastPageNumber);
        model.addAttribute("currentPage", pageIndex);

        int startPage = ((pageIndex-1)/5)*5 + 1; // 시작점
        int endPage = ((pageIndex-1)/5+1)*5; // 끝
        if(endPage > lastPageNumber) {
            endPage = lastPageNumber;
        }
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "admin/funding/settlementList";
    }

    // 정산 관리 : 상세
    @RequestMapping("settlementDetail")
    public String settlementDetail(@RequestParam("id") int id, Model model) {
        
        // 정산 정보
        Map<String, Object> settlementDto = fundingService.getSettlementDetails(id);
        model.addAttribute("settlementDto", settlementDto);

        // 프로젝트 정보
        FundingSettlementDetailsDto settlementDetails = (FundingSettlementDetailsDto)settlementDto.get("settlementDetails");
        int projectId = (int)settlementDetails.getProject_id();
        Map<String, Object> projectData = fundingService.getProjectDataForPledge(projectId);
        model.addAttribute("projectData", projectData);

        // 창작자 정보
        FundingProjectCreatorDto creatorDto = fundingService.getProjectCreatorByUserId((int)projectData.get("user_id"));
        model.addAttribute("creatorDto", creatorDto);

        // 후원 내역
        List<Map<String, Object>> settlementBackersList = fundingService.getSettlementBackersList(projectId);
        model.addAttribute("settlementBackersList", settlementBackersList);
        int settlementBackersListCount = fundingService.getSettlementBackersListCount(projectId);
        model.addAttribute("backersCount", settlementBackersListCount);

        return "admin/funding/settlementDetail";
    }

    // admin 페이지
    @RequestMapping("adminDashboard")
    public String adminDashboard() {

        return "admin/funding/adminDashboard";
    }

}
