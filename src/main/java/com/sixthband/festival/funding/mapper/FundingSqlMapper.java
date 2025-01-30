package com.sixthband.festival.funding.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sixthband.festival.funding.dto.FundingApprovalHistory;
import com.sixthband.festival.funding.dto.FundingPaymentDto;
import com.sixthband.festival.funding.dto.FundingProjectCategoryDto;
import com.sixthband.festival.funding.dto.FundingProjectCommunityCommentDto;
import com.sixthband.festival.funding.dto.FundingProjectCommunityDto;
import com.sixthband.festival.funding.dto.FundingProjectCreatorDto;
import com.sixthband.festival.funding.dto.FundingProjectDescFileDto;
import com.sixthband.festival.funding.dto.FundingProjectDto;
import com.sixthband.festival.funding.dto.FundingProjectItemDto;
import com.sixthband.festival.funding.dto.FundingProjectItemOptionDto;
import com.sixthband.festival.funding.dto.FundingProjectLikeDto;
import com.sixthband.festival.funding.dto.FundingProjectReviewDto;
import com.sixthband.festival.funding.dto.FundingProjectReviewFileDto;
import com.sixthband.festival.funding.dto.FundingProjectReviewGptDto;
import com.sixthband.festival.funding.dto.FundingProjectReviewKeywordDto;
import com.sixthband.festival.funding.dto.FundingProjectReviewSelectedKeywordDto;
import com.sixthband.festival.funding.dto.FundingProjectRewardDto;
import com.sixthband.festival.funding.dto.FundingProjectRewardItemDto;
import com.sixthband.festival.funding.dto.FundingProjectUpdateCommentDto;
import com.sixthband.festival.funding.dto.FundingProjectUpdateDto;
import com.sixthband.festival.funding.dto.FundingScheduleLogDto;
import com.sixthband.festival.funding.dto.FundingSettlementDetailsDto;
import com.sixthband.festival.funding.dto.FundingSupportDeliveryDto;
import com.sixthband.festival.funding.dto.FundingSupportDto;
import com.sixthband.festival.funding.dto.FundingSupportItemOptionDto;
import com.sixthband.festival.funding.dto.FundingSupportRewardDto;
import com.sixthband.festival.funding.dto.ProjectListDataDto;
import com.sixthband.festival.funding.dto.ProjectPledgeItemOptionDto;
import com.sixthband.festival.funding.dto.ProjectPledgeStatusDto;
import com.sixthband.festival.funding.dto.RequestKakaoPayDto;
import com.sixthband.festival.funding.dto.SupportDataDto;

@Mapper
public interface FundingSqlMapper {
    // 카테고리
    public List<FundingProjectCategoryDto> findProjectCategoryList();

    // 프로젝트 관리
    public void createFundingProjectDto(FundingProjectDto fundingProjectDto);
    public void updateFundingProjectDto(FundingProjectDto fundingProjectDto);
    public FundingProjectDto findFundingProjectDto(int id);
    public Map<String, Object> findFundingProjectEndAt(int id); // 프로젝트 id, 마감일만
    public String findFundingProjectThumbnailLocation(int id); // 프로젝트 썸네일만
    public Map<String, Object> findProjectStatus(int id); // 프로젝트 작성 진행 상황
    public List<FundingProjectDto> findProjectCreatedByUserId(@Param("user_id") int user_id, @Param("status") String status);
    public int findProjectCreatedCountByUserId(@Param("user_id") int user_id, @Param("status") String status);
    public void deleteProjectDto(int id);
    public void updateProjectStatus(@Param("id") int id, @Param("status") String status); // 프로젝트 상태 변경
    public Map<String, Object> findProjectData(int id); // 프로젝트 단일(카테고리명, 회원id)
    public Map<String, Object> findProjectDataForPledge(int id); // 프로젝트 주문서 정보 : id, 타이틀, 썸네일, 카테고리명, 창작자명
    public Map<String, Object> findProjectDataForPledgeBySupportId(int supportId); // 프로젝트 주문서 정보 - supportId 로 가져오기 : id, 타이틀, 썸네일, 카테고리명, 창작자명
    public int findDraftProjectMaxId(int user_id); // 작성 중인 프로젝트의 최댓값 id
    // 첨부파일 :: 공용으로 활용
    public void createProjectDescFile(@Param("file_table") String file_table, @Param("projectDescFileDto") FundingProjectDescFileDto projectDescFileDto); // 파일 업로드 공용
    public List<FundingProjectDescFileDto> findProjectDescFileList(@Param("file_table") String file_table, @Param("project_id") int project_id); // 파일 가져오기 공용
    public void deleteProjectDescFile(@Param("file_table") String file_table, @Param("project_id") int project_id); // 파일 삭제 : 공용
    // 프로젝트 관리 : 대시보드 - 창작자 통계
    public List<Map<String, Object>> findDailyAmountByProjectId(int project_id); // 일일 누적 후원금
    public List<Map<String, Object>> findSupportCountByGender(int project_id); // 후원자 성별 비율
    public List<Map<String, Object>> findSupportCountByAgeGroup(int project_id); // 후원자 나이대 비율

    // 프로젝트 목록 - 상품리스트 용
    public List<ProjectListDataDto> findProjectDataList(@Param("category_id") Integer category_id, @Param("pageIndex") int pageIndex, @Param("sort") int sort);
    public int findProjectDataListCount(@Param("category_id") Integer category_id);
    public ProjectPledgeStatusDto findProjectPledgeStatusByProjectId(int project_id); // 프로젝트 후원 비율 츌력
    public void increaseReadCount(int id); // 조회수 증가
    // 관심 프로젝트
    public void createProjectLike(FundingProjectLikeDto projectLikeDto);
    public void deleteProjectLike(FundingProjectLikeDto projectLikeDto);
    public int findProjectLikeByProjectIdAndUserId(@Param("project_id") int project_id, @Param("user_id") int user_id);
    public int findProjectLikeCountByProjectId(int project_id);
    public List<ProjectListDataDto> findProjectLikeDataListByUserId(int user_id); // 나의 관심 프로젝트 목록
    public int findProjectLikeDataListCountByUserId(int user_id);
    // 추천 프로젝트 : 후원 완료 후
    public List<ProjectListDataDto> findRecommendProjectDataList(int category_id);

    // 창작자
    public void createProjectCreatorDto(FundingProjectCreatorDto fundingProjectCreatorDto);
    public void updateProjectCreatorDto(FundingProjectCreatorDto fundingProjectCreatorDto);
    public int findProjectCreatorByUserId(int user_id);
    public FundingProjectCreatorDto findProjectCreatorDto(int user_id);
    public int findProjectCreatorByIdAndUserId(@Param("id") int id, @Param("user_id") int user_id);

    // 아이템
    public void createProjectItemDto(FundingProjectItemDto fundingProjectItemDto);
    public int findProjectItemCountByProjectId(int project_id);
    public List<FundingProjectItemDto> findProjectItemByProjectId(@Param("project_id") int project_id);
    public List<FundingProjectItemDto> findProjectItemById(@Param("idList") int[] idList);
    public List<FundingProjectItemDto> findProjectItemByRewardId(int reward_id);
    public FundingProjectItemDto findProjectItemByOptionId(int option_id); // 옵션 id로 가져오기
    public void deleteProjectItem(int id);
    public void deleteProjectItemByProjectId(int project_id);
    // 아이템 옵션
    public void createProjectItemOptionDto(FundingProjectItemOptionDto fundingProjectItemOptionDto);
    public int findProjectItemOptionCountByItemId(int item_id);
    public List<FundingProjectItemOptionDto> findProjectItemOptionByItemId(int item_id);
    public FundingProjectItemOptionDto findProjectItemOptionDto(int id);
    public FundingProjectItemOptionDto findProjectItemOptionByItemIdAndInId(@Param("item_id") int item_id, @Param("optionIds") int[] optionIds);
    public ProjectPledgeItemOptionDto findProjectItemOptionByItemIdAndSupportRewardId(@Param("item_id") int item_id, @Param("support_reward_id") int support_reward_id); // 후원한 아이템 옵션
    public void deleteProjectItemOptionByItemId(int item_id);
    public void deleteProjectItemOptionByProjectId(int project_id);

    // 선물
    public void createProjectRewardDto(FundingProjectRewardDto fundingProjectRewardDto);
    public List<FundingProjectRewardDto> findProjectRewardByProjectId(int project_id);
    public int findProjectRewardCountByProjectId(int project_id);
    public FundingProjectRewardDto findProjectRewardDto(int id);
    public List<FundingProjectRewardDto> findSeletedProjectRewardList(@Param("rewardIds") int[] rewardIds); // 선택한 선물 목록
    public Map<String, Object> findProjectRewardAndExpectedDate(int id); // 예상 전달일 포함
    public List<Map<String, Object>> findSupportRewardList(int support_id); // 구매한 선물 목록
    public void deleteProjectReward(int id);
    public void deleteProjectRewardByProjectId(int project_id);
    public int findSupportRewardQuantityByRewardId(int reward_id); // 선물의 판매 개수
    // 선물 아이템
    public void createProjectRewardItemDto(FundingProjectRewardItemDto fundingProjectRewardItemDto);
    public void deleteProjectRewardItemByRewardId(int reward_id);
    public void deleteProjectRewardItemByProjectId(int project_id);
    public void deleteProjectRewardItemByItemId(int item_id);

    // 후원하기 : 프로젝트
    public void createFundingSupport(FundingSupportDto fundingSupportDto);
    public void createFundingSupportReward(FundingSupportRewardDto fundingSupportRewardDto);
    public void createFundingSupportItemOption(FundingSupportItemOptionDto fundingSupportItemOptionDto);
    public void pledgeFundingSupport(@Param("id") int id, @Param("status") String status, @Param("pay_status") String pay_status); // 실 후원(주문) 진행
    public void createFundingSupportDelivery(FundingSupportDeliveryDto fundingSupportDeliveryDto);

    public FundingSupportRewardDto findSupportRewardDto(int support_reward_id); // 후원 프로젝트 선물
    public int findSupportTotalPrice(int support_id); // 후원 금액 계산

    // 후원 정보
    public FundingSupportDto findFundingSupport(int id);
    public FundingSupportDeliveryDto findFundingSupportDeliveryBySupportId(int support_id);
    public int findIsDeliveryCountBySupportReward(int support_id); // 후원선물의 배송여부 T 개수
    public int findBackersCountBySupportId(int support_id); // 후원자 개수 - 후원 완료 페이지 출력
    public int findProjectIdBySupportId(int support_id); // 후원id 로 프로젝트id 찾기
    public int findCategoryIdBySupportId(int support_id);
    // 결제정보 추가 필요
    public List<SupportDataDto> findFundingSupportListByUserId(@Param("user_id") int user_id, @Param("status") String status); // 후원 목록
    public int findFudingSupportListCountByUserId(int user_id);
    // 후원자 목록
    public List<Map<String, Object>> findBackersListByProjectUserId(@Param("user_id") int user_id, @Param("project_id") int project_id);
    public int findBackersListCountByProjectUserId(@Param("user_id") int user_id, @Param("project_id") int project_id);
    // 후원자 선물 전달 처리
    public void updateSupportDeliveryStatus(@Param("id") int id, @Param("delivery_status") String delivery_status);
    public int findSupportUserId(int id); // 후원 유저 id 단일값 가져오기
    public int findSupportCountByProjectIdAndUserId(@Param("project_id") int project_id, @Param("user_id") int user_id); // 해당 프로젝트 후원 유무 확인
    public int findSupportIdByProjectIdAndUserId(@Param("project_id") int project_id, @Param("user_id") int user_id); // 해당 프로젝트 후원 id 가져오기

    // 후기
    public List<FundingProjectReviewKeywordDto> findProjectReviewKeywordList(); // 후기 키워드 목록
    public FundingProjectReviewKeywordDto findProjectReviewKeywordDto(int id); // 리뷰 키워드 단일
    public void createProjectReviewSelectedKeyword(FundingProjectReviewSelectedKeywordDto fundingProjectReviewSelectedKeywordDto); // 선택 키워드
    public List<Map<String, Object>> findProjectReviewSelectedKeywordByReviewId(int review_id);
    public void createProjectReview(FundingProjectReviewDto fundingProjectReviewDto);
    public int findProjectReviewCountBySupportId(int support_id); // 후기 작성 여부 확인을 위한
    public List<FundingProjectReviewDto> findProjectReviewByProjectId(int project_id); // 후기 목록
    public int findProjectReviewCountByProjectId(int project_id); // 후기 목록 개수
    public void createProjectReviewFile(FundingProjectReviewFileDto fundingProjectReviewFileDto); // 리뷰 첨부파일
    public List<FundingProjectReviewFileDto> findProjectReviewFileByReviewId(int review_id);
    public FundingProjectReviewDto findProjectReviewDto(int id); // 단일 후기
    // GPT 후기 요약
    public void createProjectReviewGpt(FundingProjectReviewGptDto projectReviewGpt);
    public FundingProjectReviewGptDto findProjectReviewGptByReviewId(int review_id); // 후기 번호로 가져오기
    public int findProjectReviewGptCountByReviewId(int review_id); // 해당 후기 번호로 확인

    // 커뮤니티
    public void createProjectCommunity(FundingProjectCommunityDto projectCommunityDto);
    public List<FundingProjectCommunityDto> findProjectCommunityListByProjectId(int project_id);
    public int findProjectCommunityListCountByProjectId(int project_id);
    public void createProjectCommunityComment(FundingProjectCommunityCommentDto projectCommunityCommentDto);
    public List<FundingProjectCommunityCommentDto> findCommunityCommentListByCommunityId(int community_id);
    
    // 업데이트
    public void createProjectUpdate(FundingProjectUpdateDto projectUpdateDto);
    public List<FundingProjectUpdateDto> findProjectUpdateListByProjectId(int project_id);
    public int findProjectUpdateListCountByProjectId(int project_id);
    public FundingProjectUpdateDto findProjectUpdateDto(int id);
    public void createProjectUpdateComment(FundingProjectUpdateCommentDto projectUpdateCommentDto); // 업데이트 코멘트
    public List<FundingProjectUpdateCommentDto> findProjectUpdateCommentListByUpdateId(int update_id);
    public int findProjectUpdateCommentListCountByUpdateId(int update_id);

    // 메인
    public List<ProjectListDataDto> findProjectDataListForMain(String sortBy); // 신규 + 마감 + 랜덤
    public List<Map<String, Object>> findPopularProjectDataListForMain(); // 인기..

    // 정산 내역
    public void createSettlementDetails(FundingSettlementDetailsDto fundingSettlementDetailsDto);
    public List<FundingSettlementDetailsDto> findSettlementDetailsList(@Param("pageIndex") int index); // 목록
    public int findSettlementDetailListCount();
    public int findSettlementDetailCountByProjectId(int project_id);
    public FundingSettlementDetailsDto findSettlementDetails(int id); // 단일
    public FundingSettlementDetailsDto findSettlementDetailsByProjectId(int project_id);
    public void updateSettlementStatus(FundingSettlementDetailsDto settlementDetailsDto);
    public List<Map<String, Object>> findSettlementBackersList(int project_id); // 정산 내역 내 후원 내역 리스트
    public int findSettlementBackersListCount(int project_id);

    // 스케줄러 : 상태 처리 관련
    public void createFundingScheduleLog(FundingScheduleLogDto fundingScheduleLogDto);
    public List<FundingProjectDto> findOngoingProjectList(String status); // 진행중인 프로젝트 리스트
    public int findProjectTotalAmountByProjectId(int project_id); // 프로젝트 달성금액
    public List<FundingSupportDto> findSupportListByProjectId(int project_id); // 진행중인 프로젝트의 주문건
    public void updateSupportStatus(@Param("id") int id, @Param("status") String status, @Param("pay_status") String pay_status); // 후원 상태 업데이트

    // 관리자용
    public List<Map<String, Object>> findAdminProjectList(
        @Param("pageIndex") int pageIndex,
        @Param("categoryId") Integer categoryId,
        @Param("fromStartDate") String fromStartDate,
        @Param("toStartDate") String toStartDate,
        @Param("statusList") String[] statusList,
        @Param("fromEndDate") String fromEndDate,
        @Param("toEndDate") String toEndDate,
        @Param("searchOption") String searchOption,
        @Param("searchWord") String searchWord
    );
    public int findAdminProjectListCount(
        @Param("categoryId") Integer categoryId,
        @Param("fromStartDate") String fromStartDate,
        @Param("toStartDate") String toStartDate,
        @Param("statusList") String[] statusList,
        @Param("fromEndDate") String fromEndDate,
        @Param("toEndDate") String toEndDate,
        @Param("searchOption") String searchOption,
        @Param("searchWord") String searchWord
    );
    public List<Map<String, Object>> findAdminBackersList(@Param("pageIndex") int index);
    public int findAdminBackersListCount();
    // 심사 : 작성
    public void createApprovalHistory(FundingApprovalHistory fundingApprovalHistory);
    // 심사 : 목록 - 프로젝트별
    public List<FundingApprovalHistory> findApprovalHistoryByProjectId(int project_id);
    // 공용 관리자 대시보드 : 펀딩 상태 카운팅
    public Map<String, Object> findProjectStatusCount();

    // 카카오페이 결제
    public RequestKakaoPayDto findRequestKakaoPayDto(int support_id); // 요청 Dto
    public void createFundingPayment(FundingPaymentDto fundingPaymentDto); // 결과 Dto
}
