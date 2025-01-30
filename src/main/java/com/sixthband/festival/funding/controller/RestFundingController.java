package com.sixthband.festival.funding.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sixthband.festival.dto.ImageDto;
import com.sixthband.festival.dto.RestResponseDto;
import com.sixthband.festival.dto.UserDto;
import com.sixthband.festival.funding.dto.ProjectPledgeDataDto;
import com.sixthband.festival.funding.dto.ProjectPledgeDto;
import com.sixthband.festival.funding.dto.ReadyResponseDto;
import com.sixthband.festival.funding.dto.RequestKakaoPayDto;
import com.sixthband.festival.funding.dto.ApproveResponseDto;
import com.sixthband.festival.funding.dto.FundingPaymentDto;
import com.sixthband.festival.funding.dto.FundingProjectCommunityCommentDto;
import com.sixthband.festival.funding.dto.FundingProjectCommunityDto;
import com.sixthband.festival.funding.dto.FundingProjectCreatorDto;
import com.sixthband.festival.funding.dto.FundingProjectDto;
import com.sixthband.festival.funding.dto.FundingProjectItemDto;
import com.sixthband.festival.funding.dto.FundingProjectLikeDto;
import com.sixthband.festival.funding.dto.FundingProjectReviewDto;
import com.sixthband.festival.funding.dto.FundingProjectReviewKeywordDto;
import com.sixthband.festival.funding.dto.FundingProjectRewardDto;
import com.sixthband.festival.funding.dto.FundingProjectUpdateCommentDto;
import com.sixthband.festival.funding.dto.FundingProjectUpdateDto;
import com.sixthband.festival.funding.dto.FundingSupportDeliveryDto;
import com.sixthband.festival.funding.dto.FundingSupportDto;
import com.sixthband.festival.funding.service.FundingService;
import com.sixthband.festival.funding.service.KakaoPayService;
import com.sixthband.festival.user.service.UserService;
import com.sixthband.festival.util.ImageUtil;
import com.sixthband.festival.util.Utils;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("api/funding")
public class RestFundingController {
    @Autowired
    private FundingService fundingService;

    @Autowired
    private UserService userService;

    // 카카오페이
    @Autowired
    private KakaoPayService kakaoPayService;

    // 프로젝트 생성
    @RequestMapping("registerProject")
    public RestResponseDto registerProject(HttpSession session, FundingProjectDto params) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        // 회원여부 확인 필요
        UserDto sessionUserInfo = (UserDto)session.getAttribute("loginUser");
        if(sessionUserInfo == null) {
            restResponseDto.setResult("fail");
            restResponseDto.add("msg", "로그인 후 이용이 가능합니다."); // 로그인 실패
            return restResponseDto;
        }

        // 프로젝트 생성
        params.setUser_id(sessionUserInfo.getId());
        fundingService.registerProjectDto(params);

        // 창작자도 생성
        boolean isProjectCreator = fundingService.isProjectCreator(sessionUserInfo.getId());
        if(!isProjectCreator) { 
            FundingProjectCreatorDto projectCreatorDto = new FundingProjectCreatorDto();
            projectCreatorDto.setUser_id(sessionUserInfo.getId());
            fundingService.registerProjectCreator(projectCreatorDto);
        }

        restResponseDto.add("projectId", params.getId());

        return restResponseDto;
    }

    // 프로젝트 기획 수정
    @RequestMapping("updateProject")
    public RestResponseDto updateProject(
        FundingProjectDto params, @RequestParam(value="uploadFiles", required=false) MultipartFile uploadFiles,
        @RequestParam(value="isDescFile", required=false) boolean isDescFile,
        @RequestParam(value="old_introduce_location", required=false) String[] oldIntroduceLocation,
        @RequestParam(value="introduceFiles", required=false) MultipartFile[] introduceFiles,
        @RequestParam(value="old_teamdesc_location", required=false) String[] oldTeamdescLocation,
        @RequestParam(value="teamdescFiles", required=false) MultipartFile[] teamdescFiles,
        @RequestParam(value="old_rewarddesc_location", required=false) String[] oldRewarddescLocation,
        @RequestParam(value="rewarddescFiles", required=false) MultipartFile[] rewarddescFiles
    ) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        // 썸네일 이미지
        if(uploadFiles != null && !uploadFiles.isEmpty()) {
            String thmbnale_location = ImageUtil.saveImageAndReturnLocation(uploadFiles);
            params.setThumbnail_location(thmbnale_location);
        }

        // 첨부파일.. 업로드 : 일괄 삭제 후 재업로드
        int projectId = params.getId();

        // 프로젝트 소개
        if(isDescFile) { // 파일 삭제 후 > 기존 파일 재업로드
            fundingService.deleteProjectDescFile("funding_introduce_file", projectId); // 모든 파일 일괄 삭제
            if(oldIntroduceLocation != null) {
                fundingService.createOldProjectDescFile("funding_introduce_file", projectId, oldIntroduceLocation);
            }
        }
        List<ImageDto> introduceFileList = new ArrayList<>(); // 새로운 파일
        if(introduceFiles != null && introduceFiles.length > 0) {
            introduceFileList = ImageUtil.saveImageAndReturnDtoList(introduceFiles);
        }
        fundingService.createProjectDescFile("funding_introduce_file", projectId, introduceFileList);

        // 팀 소개
        if(isDescFile) { // 파일 삭제 후 > 기존 파일 재업로드
            fundingService.deleteProjectDescFile("funding_teamdesc_file", projectId); // 모든 파일 일괄 삭제
            if(oldTeamdescLocation != null) {
                fundingService.createOldProjectDescFile("funding_teamdesc_file", projectId, oldTeamdescLocation);
            }
        }
        List<ImageDto> teamdescFileList = new ArrayList<>(); // 새로운 파일
        if(teamdescFiles != null && teamdescFiles.length > 0) {
            teamdescFileList = ImageUtil.saveImageAndReturnDtoList(teamdescFiles);
        }
        fundingService.createProjectDescFile("funding_teamdesc_file", projectId, teamdescFileList);

        // 선물 설명
        if(isDescFile) { // 파일 삭제 후 > 기존 파일 재업로드
            fundingService.deleteProjectDescFile("funding_rewarddesc_file", projectId); // 모든 파일 일괄 삭제
            if(oldRewarddescLocation != null) {
                fundingService.createOldProjectDescFile("funding_rewarddesc_file", projectId, oldRewarddescLocation);
            }
        }
        List<ImageDto> rewarddescFileList = new ArrayList<>(); // 새로운 파일
        if(rewarddescFiles != null && rewarddescFiles.length > 0) {
            rewarddescFileList = ImageUtil.saveImageAndReturnDtoList(rewarddescFiles);
        }
        fundingService.createProjectDescFile("funding_rewarddesc_file", projectId, rewarddescFileList);
        
        // 글 수정
        fundingService.updateProjectDto(params);

        // 위 이미지 : 수정 후 ajax 처리 필요
        String uploadThumbnailLocation = fundingService.getFundingProjectThumbnailLocation(projectId);
        restResponseDto.add("uploadThumbnail", uploadThumbnailLocation);

        Map<String, Object> projectDescFileData = fundingService.getProjectDescFile(projectId);
        restResponseDto.add("projectDescFileData", projectDescFileData);

        return restResponseDto;
    }

    // 프로젝트 기획 : 프로젝트 id, 마감일 정보
    @RequestMapping("projectIdAndEndAt")
    public RestResponseDto projectIdAndEndAt(@RequestParam("id") int id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        Map<String, Object> projectEndAtData = fundingService.getFundingProjectEndAt(id);
        restResponseDto.add("projectEndAtData", projectEndAtData);

        return restResponseDto;
    }

    // 프로젝트 기획 : 창작자 정보
    @RequestMapping("updateCreator")
    public RestResponseDto updateCreatorupdateCreator(HttpSession session, FundingProjectCreatorDto params, @RequestParam("uploadProfile") MultipartFile uploadProfile) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        UserDto sessionUserInfo = (UserDto)session.getAttribute("loginUser");
        if(sessionUserInfo == null) {
            restResponseDto.setResult("fail");
            restResponseDto.add("msg", "로그인 후 이용이 가능합니다."); // 로그인 실패
            return restResponseDto;
        }

        int userId = sessionUserInfo.getId();
        params.setUser_id(userId);
        
        if(uploadProfile != null && !uploadProfile.isEmpty()) { // 파일 경로
            String profile_location = ImageUtil.saveImageAndReturnLocation(uploadProfile);
            params.setProfile_location(profile_location);
        }
        
        boolean isProjectCreator = fundingService.isProjectCreator(userId);
        if(isProjectCreator) { // 창작자 등록이 되어있는 경우
            fundingService.updateProjectCreatorDto(params);
        }else{
            fundingService.registerProjectCreator(params); // 프로젝트 생성할 때 같이 생성함으로 사실상 무관한 코드임.
        }
        
        return restResponseDto;
    }

    // 프로젝트 아이템 유무
    @RequestMapping("checkProjectItem")
    public RestResponseDto checkProjectItem(@RequestParam("id") int project_id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        int checkProjectItem = fundingService.getProjectItemCountByProjectId(project_id);
        restResponseDto.add("checkProjectItem", checkProjectItem);

        return restResponseDto;
    }

    // 프로젝트 아이템 생성 + 옵션
    @RequestMapping("registerProjectItem")
    public RestResponseDto registerProjectItem(FundingProjectItemDto params, @RequestParam("options") String[] options) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        // 아이템 생성
        fundingService.registerProjectItem(params);
        // 아이템 옵션 생성
        if(options != null && options.length > 0) {
            int itemId = params.getId();
            fundingService.registerProjectItemOption(itemId, options);
        }

        return restResponseDto;
    }

    // 프로젝트 아이템 목록
    @RequestMapping("projectItemDataList")
    public RestResponseDto projectItemDataList(@RequestParam("project_id") int project_id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        List<Map<String, Object>> projectItemDataList = fundingService.getProjectItemDataList(project_id);
        restResponseDto.add("projectItemDataList", projectItemDataList);

        // 아이템 개수
        int totalItemCount = fundingService.getProjectItemCountByProjectId(project_id);
        restResponseDto.add("totalItemCount", totalItemCount);

        return restResponseDto;
    }

    // 프로젝트 아이템 목록 : 선택한 거 출력용
    @RequestMapping("selectItemDataList")
    public RestResponseDto selectItemDataList(@RequestParam("id") int[] id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        List<Map<String, Object>> selectItemDataList = fundingService.getSelectItemDataList(id);
        restResponseDto.add("selectItemDataList", selectItemDataList);

        return restResponseDto;
    }

    // 프로젝트 아이템 삭제 + 옵션
    @RequestMapping("deleteProjectItem")
    public RestResponseDto deleteProjectItem(@RequestParam("id") int id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        fundingService.deleteProjectItemData(id);

        return restResponseDto;
    }

    // 프로젝트 선물 생성 + 선물 아이템
    @RequestMapping("registerProjectReward")
    public RestResponseDto registerProjectReward(FundingProjectRewardDto params, @RequestParam(value="itemIdList", required=false) int[] itemIdList) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        // 아이템 생성
        fundingService.registerProjectReward(params);
        // 아이템 옵션 생성
        if(itemIdList != null && itemIdList.length > 0) {
            int rewardId = params.getId();
            fundingService.registerProjectRewardItem(rewardId, itemIdList);
        }

        return restResponseDto;
    }

    // 프로젝트 선물 목록 : 프로젝트 별
    @RequestMapping("projectRewardDataList")
    public RestResponseDto projectRewardDataList(@RequestParam("project_id") int project_id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        // 선물 목록
        List<Map<String, Object>> projectRewardDataList = fundingService.getProjectRewardDataList(project_id);
        restResponseDto.add("projectRewardDataList", projectRewardDataList);
        // 선물 개수
        int totalrewardCount = fundingService.getProjectRewardCountByProjectId(project_id);
        restResponseDto.add("totalrewardCount", totalrewardCount);

        return restResponseDto;
    }

    // 프로젝트 선물 정보 : 단일
    @RequestMapping("projectRewardData")
    public RestResponseDto projectRewardData(@RequestParam("id") int id, @RequestParam(value="optionIds", required= false) int[] optionIds) { // reward_id 
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        Map<String, Object> projectRewardData = fundingService.getSelectedProjectRewardData(id, optionIds);
        restResponseDto.add("projectRewardData", projectRewardData);

        return restResponseDto;
    }

    // 프로젝트 선물 삭제 + 선물 아이템(관계)
    @RequestMapping("deleteProjectReward")
    public RestResponseDto deleteProjectReward(@RequestParam("id") int id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        fundingService.deleteProjectRewardData(id);

        return restResponseDto;
    }
    
    // 프로젝트 관리 : 대시보드
    @RequestMapping("getDashboardStatistics")
    public RestResponseDto getDashboardStatistics(@RequestParam("project_id") int project_id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        // 일일 누적 후원액
        List<Map<String, Object>> dailyAmount = fundingService.getDailyAmountByProjectId(project_id);
        restResponseDto.add("dailyAmount", dailyAmount);
        // 후원자 성별 비율
        List<Map<String, Object>> supportCountByGender = fundingService.getSupportCountByGender(project_id);
        restResponseDto.add("genderSupport", supportCountByGender);
        // 후원자 나이대 비율
        List<Map<String, Object>> supportCountByAgeGroup = fundingService.getSupportCountByAgeGroup(project_id);
        restResponseDto.add("ageGroupSupport", supportCountByAgeGroup);

        return restResponseDto;
    }

    // 프로젝트 관리 : 후원자 목록
    @RequestMapping("getBackersDataList")
    public RestResponseDto getBackersDataList(HttpSession session, @RequestParam("project_id") int project_id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        UserDto sessionUserInfo = (UserDto)session.getAttribute("loginUser");

        // 후원 목록 정보
        List<Map<String, Object>> backersDataList = fundingService.getBackersListByProjectUserId(sessionUserInfo.getId(), project_id);
        restResponseDto.add("backersDataList", backersDataList);

        int backersListCount = fundingService.getBackersListByProjectCountUserId(sessionUserInfo.getId(), project_id);
        restResponseDto.add("backersListCount", backersListCount);

        return restResponseDto;
    }

    // 후원 : 후원자 후원 상세
    @RequestMapping("getBackersDetailData")
    public RestResponseDto getBackersDetailData(@RequestParam("support_id") int support_id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        // 프로젝트+창작자 정보
        Map<String, Object> projectData = fundingService.getProjectDataForPledgeBySupportId(support_id);
        restResponseDto.add("projectData", projectData);

        // 선택한 선물, 아이템 정보 : 프로젝트 단일
        List<Map<String, Object>> projectRewardDataList = fundingService.getSupportProjectRewardDataList(support_id);
        restResponseDto.add("projectRewardDataList", projectRewardDataList);
        // 전체 금액
        int supportTotalPirce = fundingService.getSupportTotalPrice(support_id);
        restResponseDto.add("supportTotalPirce", supportTotalPirce);

        // 후원 정보
        Map<String, Object> supportData = fundingService.getSupportData(support_id);
        restResponseDto.add("supportData", supportData);

        // 후원자 정보(회원)
        FundingSupportDto supportDto = (FundingSupportDto)supportData.get("supportDto"); 
        int userId = supportDto.getUser_id();
        restResponseDto.add("userInfo", userService.getUserById(userId));

        return restResponseDto;
    }

    // 프로젝트 목록 : 회원별
    @RequestMapping("getProjectCreated")
    public RestResponseDto getProjectCreated(HttpSession session, @RequestParam(value="status", required=false) String status) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        UserDto sessionUserInfo = (UserDto)session.getAttribute("loginUser");

        // 목록
        List<FundingProjectDto> projectCreatedList = fundingService.getProjectCreated(sessionUserInfo.getId(), status);
        restResponseDto.add("projectCreatedList", projectCreatedList);
        // 목록 개수
        int projectCreatedCount = fundingService.getProjectCreatedCount(sessionUserInfo.getId(), status);
        restResponseDto.add("projectCount", projectCreatedCount);

        return restResponseDto;
    }

    // 프로젝트 삭제 : 연관 상품 포함 삭제
    @RequestMapping("deleteProjectData")
    public RestResponseDto deleteProjectData(@RequestParam("id") int id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        fundingService.deleteProjectData(id);

        return restResponseDto;
    } 

    // 프로젝트 : 심사요청
    @RequestMapping("approvalProcess")
    public RestResponseDto approvalProcess(@RequestParam("id") int id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        // 프로젝트 단계 변경 가능 확인
        boolean isApprovalStatus = fundingService.isApprovalStatus(id);
        if(!isApprovalStatus) {
            restResponseDto.setResult("fail");
            return restResponseDto;
        }

        // 상태 변경 처리
        fundingService.updateProjectStatus(id, "심사중");

        return restResponseDto;
    }
    
    // 프로젝트 : 목록 - 상품 리스트
    @RequestMapping("getDiscoverList")
    public RestResponseDto getDiscoverList(@RequestParam(value="category", required=false) Integer category_id, @RequestParam(value="page", defaultValue="1") int page, @RequestParam(value="sort", defaultValue="1") int sort) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        List<Map<String, Object>> projectDataList = fundingService.getProjectDataList(category_id, page, sort);
        restResponseDto.add("projectDataList", projectDataList);
        // 글 개수
        int projectDataListCount = fundingService.getProjectDataListCount(category_id);
        restResponseDto.add("totalCount", projectDataListCount);

        return restResponseDto;
    }
    
    // 프로젝트 후원 : 주문서 넘어갈 때 받아서 DB 에 넣기
    @RequestMapping("createProjectSupport")
    public RestResponseDto createProjectSupport(HttpSession session, @RequestBody ProjectPledgeDataDto params) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        
        // 회원 확인
        UserDto sessionUserInfo = (UserDto)session.getAttribute("loginUser");
        if(sessionUserInfo == null) {
            restResponseDto.setResult("fail");
            restResponseDto.add("msg", "로그인 후 이용이 가능합니다."); // 로그인 실패
            return restResponseDto;
        }

        // 후원여부 확인 : 후원중이면 fail 처리

        int projectId = params.getProjectId();
        List<ProjectPledgeDto> pledgeDtoList = params.getPledgeDtoList();

        // 후원하기 : 대기로 insert
        FundingSupportDto fundingSupportDto = new FundingSupportDto();
        fundingSupportDto.setUser_id(sessionUserInfo.getId());
        fundingService.registerFundingSupport(fundingSupportDto);

        // 후원 프로젝트 선물 + 아이템 옵션 : insert
        int supportId = fundingSupportDto.getId();
        fundingService.registerFundingSupportRewardAndItemOption(supportId, projectId, pledgeDtoList);

        // 후원번호만 세션에 저장
        session.setAttribute("supportId", supportId);
        
        return restResponseDto;
    }

    // 프로젝트 후원 : 주문서 - 선택한 정보 >> session
    @RequestMapping("getProjectPledgeData")
    public RestResponseDto getProjectPledgeData(HttpSession session) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        // 주문 번호 확인
        Integer supportId = (Integer)session.getAttribute("supportId");
        if(supportId == null) {
            restResponseDto.setResult("fail");
            restResponseDto.add("msg", "정상적인 방식으로 접근해주셔야 합니다.");
            return restResponseDto;
        }

        // 창작자 정보
        Map<String, Object> projectData = fundingService.getProjectDataForPledgeBySupportId(supportId);
        restResponseDto.add("projectData", projectData);

        // 선택한 선물, 아이템 정보 : 프로젝트 단일
        List<Map<String, Object>> projectRewardDataList = fundingService.getSupportProjectRewardDataList(supportId);
        restResponseDto.add("rewardDataList", projectRewardDataList);

        return restResponseDto;
    }

    // 프로젝트 후원 : 최종
    @RequestMapping("submitProjectPledge")
    public RestResponseDto submitProjectPledge(HttpSession session, FundingSupportDeliveryDto params) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        // 회원 확인
        UserDto sessionUserInfo = (UserDto)session.getAttribute("loginUser");
        if(sessionUserInfo == null) {
            restResponseDto.setResult("fail");
            restResponseDto.add("msg", "로그인 후 이용이 가능합니다."); // 로그인 실패
            return restResponseDto;
        }

        // 주문 번호 확인
        Integer supportId = (Integer)session.getAttribute("supportId");
        if(supportId == null) {
            restResponseDto.setResult("fail");
            restResponseDto.add("msg", "주문번호 확인이 필요합니다.");
            return restResponseDto;
        }

        // 후원 업데이트 > 결제 완료되면 해야함!
        // fundingService.pledgeFundingSupport(supportId);

        // 배송지 insert
        params.setSupport_id(supportId);
        fundingService.registerFundingSupportDelivery(params);

        // 카카오페이 결제 준비
        RequestKakaoPayDto requestKakaoPayDto = fundingService.getRequestKakaoPayDto(supportId); // 결제 요청으로 넘길 데이터
        ReadyResponseDto readyResponseDto = kakaoPayService.readyPayment(requestKakaoPayDto); // 결제요청 후 > 반환받은 데이터
        
        // 카카오페이 결제 이후에 필요한 정보 : session 에 저장함 >> 세션이 최선인가?
        session.setAttribute("tid", readyResponseDto.getTid());
        session.setAttribute("partner_order_id", requestKakaoPayDto.getPartner_order_id());
        session.setAttribute("partner_user_id", requestKakaoPayDto.getPartner_user_id());

        restResponseDto.add("next_redirect_pc_url", readyResponseDto.getNext_redirect_pc_url()); // pc 결제 페이지 url
        restResponseDto.add("next_redirect_mobile_url", readyResponseDto.getNext_redirect_mobile_url()); // mobile 결제 페이지 url
        restResponseDto.add("next_redirect_app_url", readyResponseDto.getNext_redirect_app_url());

        return restResponseDto;
    }

    // 결제 성공시
    @RequestMapping("payCompleted")
    public void payCompleted(@RequestParam("pg_token") String pgToken, HttpSession session, HttpServletResponse response) throws IOException {
        // 세션에 저장했던 값 가져옴
        String tid = (String)session.getAttribute("tid");
        String partner_order_id = (String)session.getAttribute("partner_order_id");
        String partner_user_id = (String)session.getAttribute("partner_user_id");

        System.out.println("결제 승인 후 :: pgToken >> " + pgToken + " /// 고유번호 tid >> " + tid);

        // 카카오 결제 요청하기
        ApproveResponseDto approveResponse = kakaoPayService.payApprove(tid, pgToken, partner_order_id, partner_user_id);

        // 후원 업데이트 > 후원상태 : 예약, 결제상태 : 결제 완료 로 처리
        Integer supportId = (Integer)session.getAttribute("supportId");
        fundingService.pledgeFundingSupport(supportId, "예약", "결제완료");

        // DB 저장
        String cid = "TC0ONETIME";
        FundingPaymentDto fundingPayment = new FundingPaymentDto();
        fundingPayment.setSupport_id(supportId);
        fundingPayment.setCid(cid);
        fundingPayment.setTid(tid);
        fundingService.registerFundingPayment(fundingPayment);

        System.out.println("fundingPayment :: DB 저장 >> " + fundingPayment);

        response.sendRedirect(Utils.FESTIVAL_URL + "/funding/pledgeCompleted"); // restController 에서 html 파일로 보내기
        // return approveResponse;
    }

    // 실패시
    @RequestMapping("payFail")
    public void payFail(HttpSession session, HttpServletResponse response) throws IOException {
        // 카카오 처리 필요.. 일단은 페이지만 넘김 > 저장 및 처리 X

        response.sendRedirect(Utils.FESTIVAL_URL + "/funding/pledgeFailed?status=fail"); 
    }

    // 취소시
    @RequestMapping("payCancel")
    public void payCancel(HttpSession session, HttpServletResponse response) throws IOException {
        // 카카오 처리 필요.. 일단은 페이지만 넘김 > 결제 자체가 안된거라 저장 및 처리 X

        response.sendRedirect(Utils.FESTIVAL_URL + "/funding/pledgeFailed?status=cancel"); 
    }

    // 후원 : 후원한 프로젝트 목록
    @RequestMapping("pledgesData")
    public RestResponseDto pledgesData(HttpSession session) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        // 회원 확인 > 추후 트랜잭션
        UserDto sessionUserInfo = (UserDto)session.getAttribute("loginUser");
        if(sessionUserInfo == null) {
            restResponseDto.setResult("fail");
            restResponseDto.add("msg", "로그인 후 이용이 가능합니다."); // 로그인 실패
            return restResponseDto;
        }

        // 후원 프로젝트 목록 - 상태별..!
        List<Map<String, Object>> reservationStatusList = fundingService.getSupportDataList(sessionUserInfo.getId(), "예약");
        restResponseDto.add("reservationStatusList", reservationStatusList);
        List<Map<String, Object>> successStatusList = fundingService.getSupportDataList(sessionUserInfo.getId(), "성공");
        restResponseDto.add("successStatusList", successStatusList);
        List<Map<String, Object>> failStatusList = fundingService.getSupportDataList(sessionUserInfo.getId(), "무산"); // 실패
        restResponseDto.add("failStatusList", failStatusList);

        // 후원 프로젝트 개수
        int totalCount = fundingService.getSupportDataListCount(sessionUserInfo.getId());
        restResponseDto.add("totalCount", totalCount);

        return restResponseDto;
    }

    // 후원 선물 상태 처리
    @RequestMapping("updateSupportDeliveryStatus")
    public RestResponseDto updateSupportDeliveryStatus(@RequestParam("support_id") int support_id, @RequestParam("delivery_status") String delivery_status) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        fundingService.updateSupportDeliveryStatus(support_id, delivery_status);

        return restResponseDto;
    }

    // 관심 프로젝트 : 추가
    @RequestMapping("projectLike")
    public RestResponseDto projectLike(HttpSession session, FundingProjectLikeDto params) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        UserDto sessionUserInfo = (UserDto)session.getAttribute("loginUser");
        params.setUser_id(sessionUserInfo.getId());

        fundingService.registerProjectLike(params);
        
        return restResponseDto;
    }

    // 관심 프로젝트 : 삭제
    @RequestMapping("projectUnLike")
    public RestResponseDto projectUnLike(HttpSession session, FundingProjectLikeDto params) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        UserDto sessionUserInfo = (UserDto)session.getAttribute("loginUser");
        params.setUser_id(sessionUserInfo.getId());

        fundingService.deleteProjectLike(params);
        
        return restResponseDto;
    }

    // 관심 프로젝트 : 여부 확인
    @RequestMapping("projectIsLiked")
    public RestResponseDto projectIsLiked(HttpSession session, @RequestParam("project_id") int project_id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        UserDto sessionUserInfo = (UserDto)session.getAttribute("loginUser");
        boolean projectIsLiked = fundingService.isProjectLiked(project_id, sessionUserInfo.getId());
        restResponseDto.add("projectIsLiked", projectIsLiked);

        int projectLikeCount = fundingService.getProjectLikeCountByProjectId(project_id);
        restResponseDto.add("projectLikeCount", projectLikeCount);

        return restResponseDto;
    }


    // 후기 : 기본 키워드 가져오기
    @RequestMapping("getReviewKeywordList")
    public RestResponseDto getReviewKeywordList() {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        List<FundingProjectReviewKeywordDto> reviewKewordList = fundingService.getProjectReviewKeywordList();
        restResponseDto.add("reviewKewordList", reviewKewordList);

        return restResponseDto;
    }
    
    // 후기 : 작성
    @RequestMapping("registerProjectReview")
    public RestResponseDto registerProjectReview(FundingProjectReviewDto params, @RequestParam("review_keyword") int[] reviewKeywords, @RequestParam(value="uploadFiles", required=false) MultipartFile[] uploadFiles) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        List<ImageDto> imageDtoList = new ArrayList<>();
        if(uploadFiles != null && uploadFiles.length > 0) {
            imageDtoList = ImageUtil.saveImageAndReturnDtoList(uploadFiles);
        }

        fundingService.registerReviewData(params, reviewKeywords, imageDtoList);

        return restResponseDto;
    }

    // 후기 정보
    @RequestMapping("projectReviewData")
    public RestResponseDto projectReviewData(@RequestParam("project_id") int project_id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        // 리뷰 정보
        List<Map<String, Object>> projectReviewDataList = fundingService.getProjectReviewDataListByProjectId(project_id);
        restResponseDto.add("projectReviewDataList", projectReviewDataList);
        // 리뷰 개수
        int projectReviewListCount = fundingService.getProjectReviewDataListCountByProjectId(project_id);
        restResponseDto.add("projectReviewListCount", projectReviewListCount);

        // GPT 리뷰 생성 - 일회성 : 주석 필수
        // fundingService.registerProjectReviewGptTest(project_id);

        return restResponseDto;
    }

    // 커뮤니티 : 작성
    @RequestMapping("registerCommnunity")
    public RestResponseDto registerCommnunity(HttpSession session, FundingProjectCommunityDto params) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        // 회원 확인
        UserDto sessionUserInfo = (UserDto)session.getAttribute("loginUser");
        params.setUser_id(sessionUserInfo.getId());

        fundingService.registerProjectCommnunity(params);

        return restResponseDto;
    }

    // 커뮤니티 : 목록
    @RequestMapping("projectCommunityData")
    public RestResponseDto projectCommunityData(@RequestParam("project_id") int project_id, HttpSession session) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        // 회원 확인
        UserDto sessionUserInfo = (UserDto)session.getAttribute("loginUser");
        if(sessionUserInfo != null) {
            // 창작자 인지 확인
            boolean isCreator = fundingService.isCreatorByProjectId(project_id, sessionUserInfo.getId());
            restResponseDto.add("isCreator", isCreator);
        }else{
            restResponseDto.add("isCreator", false);
        }

        // 커뮤니티 정보
        List<Map<String, Object>> projectCommunityDataList = fundingService.getProjectCommunityDataListByProjectId(project_id);
        restResponseDto.add("projectCommunityDataList", projectCommunityDataList);
        // 커뮤니티 글 개수
        int projectCommunityListCount = fundingService.getProjectCommunityDataListCountByProjectId(project_id);
        restResponseDto.add("projectCommunityListCount", projectCommunityListCount);
        
        return restResponseDto;
    }

    // 커뮤니티 댓글 작성
    @RequestMapping("registerCommunityComment")
    public RestResponseDto registerCommunityComment(HttpSession session, FundingProjectCommunityCommentDto params) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        // 회원 확인
        UserDto sessionUserInfo = (UserDto)session.getAttribute("loginUser");
        params.setUser_id(sessionUserInfo.getId());

        fundingService.registerProjectCommnunityComment(params);

        return restResponseDto;
    }

    // 업데이트 : 작성
    @RequestMapping("registerUpdate")
    public RestResponseDto registerUpdate(HttpSession session, FundingProjectUpdateDto params) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        // 회원 확인
        UserDto sessionUserInfo = (UserDto)session.getAttribute("loginUser");
        params.setUser_id(sessionUserInfo.getId());

        fundingService.registerProjectUpdate(params);

        return restResponseDto;
    }

    // 업데이트 : 목록
    @RequestMapping("projectUpdateDataList")
    public RestResponseDto projectUpdateDataList(@RequestParam("project_id") int project_id, HttpSession session) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        // 커뮤니티 정보
        List<Map<String, Object>> projectUpdateDataList = fundingService.getProjectUpdateDataListByProjectId(project_id);
        restResponseDto.add("projectUpdateDataList", projectUpdateDataList);
        // 커뮤니티 글 개수
        int projectUpdateListCount = fundingService.getProjectUpdateListCountByProjectId(project_id);
        restResponseDto.add("projectUpdateListCount", projectUpdateListCount);
        
        return restResponseDto;
    }

    // 업데이트 : 단일 게시물
    @RequestMapping("projectUpdateData")
    public RestResponseDto projectUpdateData(@RequestParam("id") int id, HttpSession session) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        // 게시물 정보
        Map<String, Object> projectUpdateData = fundingService.getProjectUpdateData(id);
        restResponseDto.add("projectUpdateData", projectUpdateData);

        // 댓글 정보
        List<Map<String, Object>> projectUpdateCommentList = fundingService.getProjectUpdateCommentList(id);
        restResponseDto.add("projectUpdateCommentList", projectUpdateCommentList);

        // 댓글 개수
        int projectUpdateCommentListCount = fundingService.getProjectUpdateCommentListCount(id);
        restResponseDto.add("commentListCount", projectUpdateCommentListCount);

        // 후원 유무 확인
        FundingProjectUpdateDto projectDto = (FundingProjectUpdateDto)projectUpdateData.get("projectUpdateDto");
        int projectId = projectDto.getProject_id();

        UserDto sessionUserInfo = (UserDto)session.getAttribute("loginUser");
        if(sessionUserInfo != null) {
            boolean isSupporter = fundingService.getSupportCountByProjectIdAndUserId(projectId, sessionUserInfo.getId());
            restResponseDto.add("isSupporter", isSupporter);
            // 창작자 확인
            boolean isCreator = fundingService.isCreatorByProjectId(projectId, sessionUserInfo.getId());
            restResponseDto.add("isCreator", isCreator);
        }else{
            restResponseDto.add("isSupporter", false);
            restResponseDto.add("isCreator", false);
        }

        return restResponseDto;
    }

    // 업데이트 : 작성
    @RequestMapping("registerUpdateComment")
    public RestResponseDto registerUpdateComment(HttpSession session, FundingProjectUpdateCommentDto params) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        // 회원 확인
        UserDto sessionUserInfo = (UserDto)session.getAttribute("loginUser");
        params.setUser_id(sessionUserInfo.getId());

        fundingService.registerProjectUpdateComment(params);

        return restResponseDto;
    }

    // 관심 프로젝트 :: 여기 확인하기
    @RequestMapping("getLikedProjectList")
    public RestResponseDto getLikedProjectList(HttpSession session) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        // 회원 확인
        UserDto sessionUserInfo = (UserDto)session.getAttribute("loginUser");
        if(sessionUserInfo == null) {
            restResponseDto.setResult("fail");
            restResponseDto.add("msg", "로그인 후 이용이 가능합니다."); // 로그인 실패
            return restResponseDto;
        }

        List<Map<String, Object>> projectLikeDataList = fundingService.getProjectLikeDataList(sessionUserInfo.getId());
        restResponseDto.add("projectLikeDataList", projectLikeDataList);
        // 글 개수
        int projectLikeDataListCount = fundingService.getProjectLikeDataListCountByUserId(sessionUserInfo.getId());
        restResponseDto.add("totalCount", projectLikeDataListCount);

        return restResponseDto;
    }
}
