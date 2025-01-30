package com.sixthband.festival.funding.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import com.sixthband.festival.dto.UserDto;
import com.sixthband.festival.funding.dto.FundingProjectCategoryDto;
import com.sixthband.festival.funding.dto.FundingProjectCreatorDto;
import com.sixthband.festival.funding.dto.FundingProjectDto;
import com.sixthband.festival.funding.dto.FundingSettlementDetailsDto;
import com.sixthband.festival.funding.dto.ProjectPledgeStatusDto;
import com.sixthband.festival.funding.service.FundingService;
import com.sixthband.festival.util.Utils;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("funding")
public class FundingController {
    @Autowired
    private FundingService fundingService;

    @RequestMapping("")
    public String mainPage(Model model) {
        // 신규 프로젝트
        List<Map<String, Object>> newProjectDataList = fundingService.getProjectDataListForMain("new");
        model.addAttribute("newProjectDataList", newProjectDataList);

        // 마감임박 프로젝트
        List<Map<String, Object>> endProjectDataList = fundingService.getProjectDataListForMain("endAt");
        model.addAttribute("endProjectDataList", endProjectDataList);

        // 인기 프로젝트
        List<Map<String, Object>> popularProjectDataList = fundingService.getPorpularProjectDataListForMain();
        model.addAttribute("popularProjectDataList", popularProjectDataList);

        // 랜덤 프로젝트
        List<Map<String, Object>> randProjectDataList = fundingService.getProjectDataListForMain("rand");
        model.addAttribute("randProjectDataList", randProjectDataList);

        return "funding/mainPage";
    }

    // 만든 프로젝트
    @RequestMapping("projectsCreated")
    public String projectCreated(HttpSession session) {
        // 회원여부
        UserDto sessionUserInfo = (UserDto)session.getAttribute("loginUser");
        if(sessionUserInfo == null) {
            return "redirect:" + Utils.FESTIVAL_URL + "/user/loginPage";
        }

        return "funding/projectsCreated";
    }
    
    // 프로젝트 생성
    @RequestMapping("projectEditorStart")
    public String projectEditorStart(HttpSession session, Model model) {
        // 회원여부
        UserDto sessionUserInfo = (UserDto)session.getAttribute("loginUser");
        if(sessionUserInfo == null) {
            return "redirect:" + Utils.FESTIVAL_URL + "/user/loginPage";
        }

        // 작성중인 프로젝트 확인
        int draftProjectMaxId = fundingService.getDraftProjectMaxId(sessionUserInfo.getId());
        model.addAttribute("isWritingId", draftProjectMaxId);

        // 카테고리
        List<FundingProjectCategoryDto> projectCategoryList = fundingService.getProjectCategoryList();
        model.addAttribute("projectCategoryList", projectCategoryList);

        return "funding/projectEditorStart";
    }

    // 프로젝트 관리 - 기획
    @RequestMapping("projectEditorManagement")
    public String projectEditorManagement(HttpSession session, @RequestParam("id") int id, Model model) {
        // 상단 : 프로젝트 정보
        Map<String, Object> projectData = fundingService.getProjectDataForPledge(id);
        model.addAttribute("projectData", projectData);

        boolean isFundingStatus = fundingService.isFundingStatus(id);
        model.addAttribute("isFundingStatus", isFundingStatus);

        // 회원여부 => 트랜젝션 처리하기
        UserDto sessionUserInfo = (UserDto)session.getAttribute("loginUser");
        if(sessionUserInfo == null) {
            return "redirect:" + Utils.FESTIVAL_URL + "/user/loginPage";
        }

        // 프로젝트 기획 단계별 진행상태
        Map<String, Object> projectStatus = fundingService.getProjectStatus(id);
        model.addAttribute("projectStatus", projectStatus);

        return "funding/projectEditorManagement";
    }
    
    // 프로젝트 관리 - 기획 : 기본 정보
    @RequestMapping("management/default")
    public String managementDefault(@RequestParam("id") int id, Model model) {
        // 카테고리
        List<FundingProjectCategoryDto> projectCategoryList = fundingService.getProjectCategoryList();
        model.addAttribute("projectCategoryList", projectCategoryList);

        // 글 정보
        FundingProjectDto projectDto = fundingService.getFundingProjectDto(id);
        model.addAttribute("projectDto", projectDto);

        return "funding/management/default";
    }

    // 프로젝트 관리 - 기획 : 펀딩 계획
    @RequestMapping("management/funding")
    public String managementFunding(@RequestParam("id") int id, Model model) {

        // 글 정보
        FundingProjectDto projectDto = fundingService.getFundingProjectDto(id);
        model.addAttribute("projectDto", projectDto);

        // 시간 : 내일 부터 설정 가능
        LocalDate tomorrowDay = LocalDate.now().plusDays(1);
        model.addAttribute("tomorrowDay", tomorrowDay);

        return "funding/management/funding";
    }

    // 프로젝트 관리 - 기획 : 선물 구성
    @RequestMapping("management/reward")
    public String managementReward(@RequestParam("id") int id, Model model) {

        return "funding/management/reward";
    }

    // 프로젝트 관리 - 기획 : 프로젝트 계획
    @RequestMapping("management/story")
    public String managementStory(@RequestParam("id") int id, Model model) {

        // 글 정보
        FundingProjectDto projectDto = fundingService.getFundingProjectDto(id);
        model.addAttribute("projectDto", projectDto);

        // 첨부파일
        Map<String, Object> projectFile = fundingService.getProjectDescFile(id);
        model.addAttribute("projectFile", projectFile);

        return "funding/management/story";
    }

    // 프로젝트 관리 - 기획 : 창작자 정보
    @RequestMapping("management/creator")
    public String managementCreator(HttpSession session, Model model) {

        // 회원여부
        UserDto sessionUserInfo = (UserDto)session.getAttribute("loginUser");
        if(sessionUserInfo == null) {
            return "redirect:" + Utils.FESTIVAL_URL + "/user/loginPage";
        }

        FundingProjectCreatorDto creatorDto = fundingService.getProjectCreatorByUserId(sessionUserInfo.getId());
        model.addAttribute("creatorDto", creatorDto);
        // System.out.println("creatorDto ~~~~~" + creatorDto);

        return "funding/management/creator";
    }

    // 프로젝트 관리 - 대시보드
    @RequestMapping("projectEditorDashboard")
    public String projectEditorDashboard(HttpSession session, @RequestParam("id") int id, Model model) {
        
        // 상단 : 프로젝트 정보
        Map<String, Object> projectData = fundingService.getProjectDataForPledge(id);
        model.addAttribute("projectData", projectData);
        boolean isFundingStatus = fundingService.isFundingStatus(id);
        model.addAttribute("isFundingStatus", isFundingStatus);

        // 회원여부 => 트랜젝션 처리하기
        UserDto sessionUserInfo = (UserDto)session.getAttribute("loginUser");
        if(sessionUserInfo == null) {
            return "redirect:" + Utils.FESTIVAL_URL + "/user/loginPage";
        }

        // 프로젝트 기획 단계별 진행상태
        Map<String, Object> projectStatus = fundingService.getProjectStatus(id);
        model.addAttribute("projectStatus", projectStatus);

        // 프로젝트 정보 : 프로젝트+창작자
        Map<String, Object> projectDetailData = fundingService.getProjectData(id);
        model.addAttribute("projectDetailData", projectDetailData);
        // 프로젝트 달성률
        ProjectPledgeStatusDto pledgeStatus = fundingService.getProjectPledgeStatusByProjectId(id);
        model.addAttribute("pledgeStatus", pledgeStatus);
        
        return "funding/projectEditorDashboard";
    }

    // 프로젝트 관리 - 후원자 관리
    @RequestMapping("projectEditorBackers")
    public String projectEditorBackers(HttpSession session, @RequestParam("id") int id, Model model) {

        // 상단 : 프로젝트 정보
        Map<String, Object> projectData = fundingService.getProjectDataForPledge(id);
        model.addAttribute("projectData", projectData);
        boolean isFundingStatus = fundingService.isFundingStatus(id);
        model.addAttribute("isFundingStatus", isFundingStatus);

        // 회원여부 => 트랜젝션 처리하기
        UserDto sessionUserInfo = (UserDto)session.getAttribute("loginUser");
        if(sessionUserInfo == null) {
            return "redirect:" + Utils.FESTIVAL_URL + "/user/loginPage";
        }

        // 프로젝트 기획 단계별 진행상태
        Map<String, Object> projectStatus = fundingService.getProjectStatus(id);
        model.addAttribute("projectStatus", projectStatus);

        return "funding/projectEditorBackers";
    }

    // 프로젝트 관리 - 모금액 명세
    @RequestMapping("projectEditorPledges")
    public String projectEditorPledges(HttpSession session, @RequestParam("id") int id, Model model) {

        // 상단 : 프로젝트 정보
        Map<String, Object> projectData = fundingService.getProjectDataForPledge(id);
        model.addAttribute("projectData", projectData);
        boolean isFundingStatus = fundingService.isFundingStatus(id);
        model.addAttribute("isFundingStatus", isFundingStatus);

        // 회원여부 => 트랜젝션 처리하기
        UserDto sessionUserInfo = (UserDto)session.getAttribute("loginUser");
        if(sessionUserInfo == null) {
            return "redirect:" + Utils.FESTIVAL_URL + "/user/loginPage";
        }

        // 프로젝트 기획 단계별 진행상태
        Map<String, Object> projectStatus = fundingService.getProjectStatus(id);
        model.addAttribute("projectStatus", projectStatus);

        // 정산 정보
        FundingSettlementDetailsDto settlementDetailsDto = fundingService.getSettlementDetailsByProjectId(id);
        if(settlementDetailsDto != null) {
            Map<String, Object> settlementDto = fundingService.getSettlementDetails(settlementDetailsDto.getId());
            model.addAttribute("settlementDto", settlementDto);
        }
        boolean isSettlementCount = fundingService.isSettlementDetailCount(id);
        model.addAttribute("isSettlementCount", isSettlementCount);
        // 프로젝트 정보
        Map<String, Object> settlementProjectData = fundingService.getProjectDataForPledge(id);
        model.addAttribute("settlementProjectData", settlementProjectData);
        // 창작자 정보
        FundingProjectCreatorDto creatorDto = fundingService.getProjectCreatorByUserId((int)settlementProjectData.get("user_id"));
        model.addAttribute("creatorDto", creatorDto);
        // 후원자 수
        int settlementBackersListCount = fundingService.getSettlementBackersListCount(id);
        model.addAttribute("backersCount", settlementBackersListCount);

        return "funding/projectEditorPledges";
    }

    // 프로젝트 목록
    @RequestMapping("discover")
    public String discover() {

        return "funding/discover";
    }
    
    // 프로젝트 상세
    @RequestMapping("detailPage")
    public String detailPage(@RequestParam("id") int id, Model model, HttpSession session) {

        // 프로젝트 정보 : 프로젝트+창작자
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

        // 프로젝트 달성률
        ProjectPledgeStatusDto pledgeStatus = fundingService.getProjectPledgeStatusByProjectId(id);
        model.addAttribute("pledgeStatus", pledgeStatus);

        // 상품 정보
        List<Map<String, Object>> projectRewardDataList = fundingService.getProjectRewardDataList(id);
        model.addAttribute("projectRewardDataList", projectRewardDataList);

        // 첨부파일
        Map<String, Object> projectFile = fundingService.getProjectDescFile(id);
        model.addAttribute("projectFile", projectFile);

        // 게시물 개수
        int updateListCount = fundingService.getProjectUpdateListCountByProjectId(id); // 업데이트 게시판
        model.addAttribute("updateListCount", updateListCount);
        int communityListCount = fundingService.getProjectCommunityDataListCountByProjectId(id); // 커뮤니티 게시판
        model.addAttribute("communityListCount", communityListCount);
        int reviewListCount = fundingService.getProjectReviewDataListCountByProjectId(id); // 후기 게시판
        model.addAttribute("reviewListCount", reviewListCount);

        // 회원 확인
        UserDto sessionUserInfo = (UserDto)session.getAttribute("loginUser");
        if(sessionUserInfo != null) {
            // 커뮤니티 : 작성 확인
            boolean isSupporter = fundingService.getSupportCountByProjectIdAndUserId(id, sessionUserInfo.getId());
            model.addAttribute("isSupporter", isSupporter);
            if(isSupporter) {
                int supportId = fundingService.getSupportIdByProjectIdAndUserId(id, sessionUserInfo.getId());
                model.addAttribute("supportId", supportId);
            }
            // 업데이트 : 창작자 확인
            boolean isCreator = fundingService.isCreatorByProjectId(id, sessionUserInfo.getId());
            model.addAttribute("isCreator", isCreator);
            // 좋아요 여부
            boolean projectIsLiked = fundingService.isProjectLiked(id, sessionUserInfo.getId());
            model.addAttribute("projectIsLiked", projectIsLiked);
        }else{
            model.addAttribute("isSupporter", false);
            model.addAttribute("isCreator", false);
            model.addAttribute("projectIsLiked", false);
        }
        
        // 조회수 증가
        fundingService.increaseReadCount(id);
        // 관심 개수
        int projectLikeCount = fundingService.getProjectLikeCountByProjectId(id);
        model.addAttribute("projectLikeCount", projectLikeCount);

        return "funding/detailPage";
    }

    // 프로젝트 후원
    @RequestMapping("pledgePage")
    public String pledgePage(HttpSession session, Model model) {

        // 회원여부
        UserDto sessionUserInfo = (UserDto)session.getAttribute("loginUser");
        if(sessionUserInfo == null) {
            return "redirect:" + Utils.FESTIVAL_URL + "/user/loginPage";
        }
        // 회원 정보
        model.addAttribute("sessionUserInfo", sessionUserInfo);

        // 주문 번호 확인
        Integer supportId = (Integer)session.getAttribute("supportId");
        if(supportId == null) {
            return "redirect:" + Utils.FESTIVAL_URL + "/funding";
        }

        // 프로젝트+창작자 정보
        Map<String, Object> projectData = fundingService.getProjectDataForPledgeBySupportId(supportId);
        model.addAttribute("projectData", projectData);

        // 프로젝트 후원율
        ProjectPledgeStatusDto pledgeStatus = fundingService.getProjectPledgeStatusByProjectId((int)projectData.get("id"));
        model.addAttribute("pledgeStatus", pledgeStatus);

        // 선택한 선물, 아이템 정보 : 프로젝트 단일
        List<Map<String, Object>> projectRewardDataList = fundingService.getSupportProjectRewardDataList(supportId);
        model.addAttribute("projectRewardDataList", projectRewardDataList);

        // 전체 금액
        int supportTotalPirce = fundingService.getSupportTotalPrice(supportId);
        model.addAttribute("supportTotalPirce", supportTotalPirce);

        return "funding/pledgePage";
    }

    // 프로젝트 후원 완료
    @RequestMapping("pledgeCompleted")
    public String pledgeCompleted(HttpSession session, Model model) {
        // 주문 번호 확인
        Integer supportId = (Integer)session.getAttribute("supportId");

        // 후원자 수
        int backersCount = fundingService.getBackersCountBySupportId(supportId);
        model.addAttribute("backersCount", backersCount);

        // 추천 프로젝트 : 구매 프로젝트의 카테고리 우선 노출
        List<Map<String, Object>> recommendDataList = fundingService.getRecommendProjectDataList(supportId);
        model.addAttribute("projectDataList", recommendDataList);

        return "funding/pledgeCompleted";
    }

    // 프로젝트 후원 실패
    @RequestMapping("pledgeFailed")
    public String pledgeFailed(HttpSession session, Model model, @RequestParam("status") String status) {
        model.addAttribute("status", status);

        // 주문 번호 확인
        Integer supportId = (Integer)session.getAttribute("supportId");

        int projectId = fundingService.getProjectIdBySupportId(supportId);
        model.addAttribute("projectId", projectId);

        return "funding/pledgeFailed";
    }

    // 프로젝트 후원 상세
    @RequestMapping("pledgeDetail")
    public String pledgeDetail(@RequestParam("id") int id, HttpSession session, Model model) {

        // 회원여부
        UserDto sessionUserInfo = (UserDto)session.getAttribute("loginUser");
        if(sessionUserInfo == null) {
            return "redirect:" + Utils.FESTIVAL_URL + "/user/loginPage";
        }
        // 회원 정보
        model.addAttribute("sessionUserInfo", sessionUserInfo);

        // 프로젝트+창작자 정보
        Map<String, Object> projectData = fundingService.getProjectDataForPledgeBySupportId(id);
        model.addAttribute("projectData", projectData);
        // 프로젝트 후원율
        ProjectPledgeStatusDto pledgeStatus = fundingService.getProjectPledgeStatusByProjectId((int)projectData.get("id"));
        model.addAttribute("pledgeStatus", pledgeStatus);

        // 선택한 선물, 아이템 정보 : 프로젝트 단일
        List<Map<String, Object>> projectRewardDataList = fundingService.getSupportProjectRewardDataList(id);
        model.addAttribute("projectRewardDataList", projectRewardDataList);
        // 전체 금액
        int supportTotalPirce = fundingService.getSupportTotalPrice(id);
        model.addAttribute("supportTotalPirce", supportTotalPirce);

        // 후원 정보
        Map<String, Object> supportData = fundingService.getSupportData(id);
        model.addAttribute("supportData", supportData);
        // 후기 작성 여부
        boolean isReviewWrite = fundingService.isProjectReviewWrite(id);
        model.addAttribute("isReviewWrite", isReviewWrite);

        return "funding/pledgeDetail";
    }

    // 후원한 프로젝트
    @RequestMapping("pledges")
    public String pledges(Model model) {

        return "funding/pledges";
    }

    // 관심 프로젝트
    @RequestMapping("projectsLiked")
    public String projectsLiked() {

        return "funding/projectsLiked";
    }
}
