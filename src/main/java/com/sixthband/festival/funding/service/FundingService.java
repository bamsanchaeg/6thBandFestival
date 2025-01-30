package com.sixthband.festival.funding.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sixthband.festival.dto.ImageDto;
import com.sixthband.festival.dto.UserDto;
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
import com.sixthband.festival.funding.dto.FundingSettlementDetailsDto;
import com.sixthband.festival.funding.dto.FundingSupportDeliveryDto;
import com.sixthband.festival.funding.dto.FundingSupportDto;
import com.sixthband.festival.funding.dto.FundingSupportItemOptionDto;
import com.sixthband.festival.funding.dto.FundingSupportRewardDto;
import com.sixthband.festival.funding.dto.ProjectListDataDto;
import com.sixthband.festival.funding.dto.ProjectPledgeDto;
import com.sixthband.festival.funding.dto.ProjectPledgeItemOptionDto;
import com.sixthband.festival.funding.dto.ProjectPledgeStatusDto;
import com.sixthband.festival.funding.dto.RequestKakaoPayDto;
import com.sixthband.festival.funding.dto.SupportDataDto;
import com.sixthband.festival.funding.mapper.FundingSqlMapper;
import com.sixthband.festival.user.mapper.UserSqlMapper;

@Service
public class FundingService {
    @Autowired
    private FundingSqlMapper fundingSqlMapper;

    @Autowired
    private UserSqlMapper userSqlMapper;

    // ChatGPT
    @Autowired
    private OpenAiGptService openAiGptService;

    // 카테고리 목록
    public List<FundingProjectCategoryDto> getProjectCategoryList() {
        return fundingSqlMapper.findProjectCategoryList();
    }

    // 프로젝트 : 생성
    public void registerProjectDto(FundingProjectDto fundingProjectDto) {
        fundingSqlMapper.createFundingProjectDto(fundingProjectDto);
    }
    // 프로젝트 : 수정
    public void updateProjectDto(FundingProjectDto fundingProjectDto) {
        fundingSqlMapper.updateFundingProjectDto(fundingProjectDto);
    }
    // 프로젝트 : 첨부파일 삽입
    public void createProjectDescFile(String file_table, int project_id, List<ImageDto> imageDtoList) {
        for(ImageDto image : imageDtoList) {
            if(image == null) continue;
            FundingProjectDescFileDto projectDescFile = new FundingProjectDescFileDto();
            projectDescFile.setProject_id(project_id);
            projectDescFile.setLocation(image.getLocation());
            fundingSqlMapper.createProjectDescFile(file_table, projectDescFile);
        }
    }
    // 프로젝트 : 기존 첨부파일 삽입
    public void createOldProjectDescFile(String file_table, int project_id, String[] locationList) {
        for(String location : locationList) {
            if(location == null) continue;
            FundingProjectDescFileDto projectDescFile = new FundingProjectDescFileDto();
            projectDescFile.setProject_id(project_id);
            projectDescFile.setLocation(location);
            fundingSqlMapper.createProjectDescFile(file_table, projectDescFile);
        }
    }
    // 프로젝트 : 파일 일괄 삭제
    public void deleteProjectDescFile(String file_table, int project_id) {
        fundingSqlMapper.deleteProjectDescFile(file_table, project_id);
    }
    // 프로젝트 : 첨부파일 출력
    public Map<String, Object> getProjectDescFile(int project_id) {
        Map<String, Object> map = new HashMap<>();

        // 프로젝트 소개
        List<FundingProjectDescFileDto> introduceFileList = fundingSqlMapper.findProjectDescFileList("funding_introduce_file", project_id);
        map.put("introduceFileList", introduceFileList);
        // 팀 소개
        List<FundingProjectDescFileDto> teamdescFileList = fundingSqlMapper.findProjectDescFileList("funding_teamdesc_file", project_id);
        map.put("teamdescFileList", teamdescFileList);
        // 선물 설명
        List<FundingProjectDescFileDto> rewarddescFileList = fundingSqlMapper.findProjectDescFileList("funding_rewarddesc_file", project_id);
        map.put("rewarddescFileList", rewarddescFileList);
        
        return map;
    }

    // 프로젝트 : 확인
    public FundingProjectDto getFundingProjectDto(int id) {
        return fundingSqlMapper.findFundingProjectDto(id);
    }
    // 프로젝트 : id, 마감일 가져오기
    public Map<String, Object> getFundingProjectEndAt(int id) {
        return fundingSqlMapper.findFundingProjectEndAt(id);
    }
    // 프로젝트 : 썸네일 가져오기
    public String getFundingProjectThumbnailLocation(int id) {
        return fundingSqlMapper.findFundingProjectThumbnailLocation(id);
    }
    // 프로젝트 : 목록 - 회원별
    public List<FundingProjectDto> getProjectCreated(int user_id, String status) {
        return fundingSqlMapper.findProjectCreatedByUserId(user_id, status);
    }
    // 프로젝트 : 목록 개수 - 회원별
    public int getProjectCreatedCount(int user_id, String status) {
        return fundingSqlMapper.findProjectCreatedCountByUserId(user_id, status);
    }
    // 프로젝트 : 작성중인 프로젝트의 최대 id 값
    public int getDraftProjectMaxId(int user_id) {
        return fundingSqlMapper.findDraftProjectMaxId(user_id);
    }
    // 프로젝트 : 기획 단계별 진행상황 가져오기
    public Map<String, Object> getProjectStatus(int id) {
        Map<String, Object> projectStatus = fundingSqlMapper.findProjectStatus(id);

        String managementStatus = "작성중"; // 전체 진행 상태
        if(projectStatus.get("defaultStatus").equals("작성완료") 
            && projectStatus.get("fundingStatus").equals("작성완료")
            && projectStatus.get("storyStatus").equals("작성완료")
            && projectStatus.get("rewardStatus").equals("작성완료")
            && projectStatus.get("creatorStatus").equals("작성완료")
        ) {
            managementStatus = "작성완료";
        }

        projectStatus.put("managementStatus", managementStatus);

        return projectStatus;
    }
    // 프로젝트 : 삭제 - 아이템, 아이템 옵션, 선물, 선물아이템 + 심사이력, 태그
    public void deleteProjectData(int id) {
        // 선물 아이템
        fundingSqlMapper.deleteProjectRewardItemByProjectId(id);
        // 선물
        fundingSqlMapper.deleteProjectRewardByProjectId(id);
        // 아이템 옵션
        fundingSqlMapper.deleteProjectItemOptionByProjectId(id);
        // 아이템
        fundingSqlMapper.deleteProjectItemByProjectId(id);
        // 프로젝트
        fundingSqlMapper.deleteProjectDto(id);
        // 심사이력, 태그
    }
    // 프로젝트 : 심사요청 가능 확인
    public boolean isApprovalStatus(int id) {
        boolean isApprovalStatus = true; // 가능 여부

        // 프로젝트 현재 상태
        FundingProjectDto fundingProjectDto = fundingSqlMapper.findFundingProjectDto(id);
        if(!(fundingProjectDto.getStatus().equals("작성중") || fundingProjectDto.getStatus().equals("반려"))) {
            isApprovalStatus = false;
        }

        // 프로젝트 작성 상태
        Map<String, Object> projectStatus = fundingSqlMapper.findProjectStatus(id);
        if(!projectStatus.get("defaultStatus").equals("작성완료") 
            || !projectStatus.get("fundingStatus").equals("작성완료")
            || !projectStatus.get("storyStatus").equals("작성완료")
            || !projectStatus.get("rewardStatus").equals("작성완료")
            || !projectStatus.get("creatorStatus").equals("작성완료")
        ) {
            isApprovalStatus = false;
        }
        
        return isApprovalStatus;
    }
    // 프로젝트 상태 변경
    public void updateProjectStatus(int id, String status) {
        fundingSqlMapper.updateProjectStatus(id, status);
    }
    // 프로젝트 정보 : 단일
    public Map<String, Object> getProjectData(int id) {
        Map<String, Object> map = new HashMap<>();

        // 프로젝트 정보(카테고리명, 회원id 포함)
        Map<String, Object> projectData = fundingSqlMapper.findProjectData(id);
        map.put("project", projectData);
        int userId = (int)projectData.get("user_id");
        // 창작자 정보
        FundingProjectCreatorDto creatorDto = fundingSqlMapper.findProjectCreatorDto(userId);
        map.put("creatorDto", creatorDto);

        return map;
    }
    // 프로젝트 정보 : 일부 - 프로젝트명, 썸네일, 카테고리명, 창작자명
    public Map<String, Object> getProjectDataForPledge(int id) {
        return fundingSqlMapper.findProjectDataForPledge(id);
    }
    // 프로젝트 정보 - support id 로 가져오기 : 일부 - 프로젝트명, 썸네일, 카테고리명, 창작자명
    public Map<String, Object> getProjectDataForPledgeBySupportId(int supportId) {
        return fundingSqlMapper.findProjectDataForPledgeBySupportId(supportId);
    }
    // 프로젝트 상태
    public boolean isFundingStatus(int id) {
        Map<String, Object> projectData = fundingSqlMapper.findProjectDataForPledge(id);
        String projectStatus = (String)projectData.get("project_status");

        if(projectStatus.equals("진행중") || projectStatus.equals("성공") || projectStatus.equals("무산")) {
            return true;
        }
        return false;
    }

    // 프로젝트 : 목록 - 상품리스트
    public List<Map<String, Object>> getProjectDataList(Integer category_id, int page, int sort) {

        List<Map<String, Object>> result = new ArrayList<>();
        List<ProjectListDataDto> projectDataList = fundingSqlMapper.findProjectDataList(category_id, (page-1)*10, sort);
        for(ProjectListDataDto projectData : projectDataList) {
            Map<String, Object> map = new HashMap<>();
            // 프로젝트 정보
            map.put("projectData", projectData);

            // 후원 비율
            ProjectPledgeStatusDto pledgeStatus = fundingSqlMapper.findProjectPledgeStatusByProjectId(projectData.getId());
            map.put("pledgeStatus", pledgeStatus);

            result.add(map);
        }

        return result;
    }
    // 프로젝트 : 목록 개수
    public int getProjectDataListCount(Integer category_id) {
        return fundingSqlMapper.findProjectDataListCount(category_id);
    }

    // 프로젝트 정보 : 후원 상태
    public ProjectPledgeStatusDto getProjectPledgeStatusByProjectId(int project_id) {
        return fundingSqlMapper.findProjectPledgeStatusByProjectId(project_id);
    }
    
    // 프로젝트 : 조회수 증가
    public void increaseReadCount(int id) {
        fundingSqlMapper.increaseReadCount(id);
    }

    // 관심 프로젝트 : 생성
    public void registerProjectLike(FundingProjectLikeDto projectLikeDto) {
        fundingSqlMapper.createProjectLike(projectLikeDto);
    }
    // 관심 프로젝트 : 삭제
    public void deleteProjectLike(FundingProjectLikeDto projectLikeDto) {
        fundingSqlMapper.deleteProjectLike(projectLikeDto);
    }
    // 관심 프로젝트 : 유무
    public boolean isProjectLiked(int project_id, int user_id) {
        return fundingSqlMapper.findProjectLikeByProjectIdAndUserId(project_id, user_id) > 0;
    }
    // 관심 개수 : 프로젝트 별
    public int getProjectLikeCountByProjectId(int project_id) {
        return fundingSqlMapper.findProjectLikeCountByProjectId(project_id);
    }

    // 관심 프로젝트 : 목록
    public List<Map<String, Object>> getProjectLikeDataList(int user_id) {

        List<Map<String, Object>> result = new ArrayList<>();
        List<ProjectListDataDto> projectLikeDataList = fundingSqlMapper.findProjectLikeDataListByUserId(user_id);
        for(ProjectListDataDto projectLikeData : projectLikeDataList) {
            Map<String, Object> map = new HashMap<>();
            // 프로젝트 정보
            map.put("projectData", projectLikeData);

            // 후원 비율
            ProjectPledgeStatusDto pledgeStatus = fundingSqlMapper.findProjectPledgeStatusByProjectId(projectLikeData.getId());
            map.put("pledgeStatus", pledgeStatus);

            result.add(map);
        }

        return result;
    }
    // 관심 프로젝트 : 목록 개수
    public int getProjectLikeDataListCountByUserId(int user_id) {
        return fundingSqlMapper.findProjectLikeDataListCountByUserId(user_id);
    }

    // 추천 프로젝트 : 후원 완료 후
    public List<Map<String, Object>> getRecommendProjectDataList(int support_id) {
        int categoryId = fundingSqlMapper.findCategoryIdBySupportId(support_id);

        List<Map<String, Object>> result = new ArrayList<>();
        List<ProjectListDataDto> projectDataList = fundingSqlMapper.findRecommendProjectDataList(categoryId);
        for(ProjectListDataDto projectData : projectDataList) {
            Map<String, Object> map = new HashMap<>();
            // 프로젝트 정보
            map.put("projectData", projectData);

            // 후원 비율
            ProjectPledgeStatusDto pledgeStatus = fundingSqlMapper.findProjectPledgeStatusByProjectId(projectData.getId());
            map.put("pledgeStatus", pledgeStatus);

            result.add(map);
        }

        return result;
    }

    // 창작자 : 작성
    public void registerProjectCreator(FundingProjectCreatorDto fundingProjectCreatorDto) {
        fundingSqlMapper.createProjectCreatorDto(fundingProjectCreatorDto);
    }
    // 창작자 : 수정
    public void updateProjectCreatorDto(FundingProjectCreatorDto fundingProjectCreatorDto) {
        fundingSqlMapper.updateProjectCreatorDto(fundingProjectCreatorDto);
    }
    // 창작자 : 등록 확인
    public boolean isProjectCreator(int user_id) {
        return fundingSqlMapper.findProjectCreatorByUserId(user_id) > 0;
    }
    // 창작자 : 정보
    public FundingProjectCreatorDto getProjectCreatorByUserId(int user_id) {
        return fundingSqlMapper.findProjectCreatorDto(user_id);
    }
    // 창작자 : 인지 확인
    public boolean isCreatorByProjectId(int id, int user_id) {
        return fundingSqlMapper.findProjectCreatorByIdAndUserId(id, user_id) > 0;
    }

    // 아이템 : 생성
    public void registerProjectItem(FundingProjectItemDto fundingProjectItemDto) {
        fundingSqlMapper.createProjectItemDto(fundingProjectItemDto);
    }
    // 아이템 : 목록
    public List<Map<String, Object>> getProjectItemDataList(int project_id){
        List<Map<String, Object>> result = new ArrayList<>();
        List<FundingProjectItemDto> projectItemList = fundingSqlMapper.findProjectItemByProjectId(project_id);
        for(FundingProjectItemDto projectItemDto : projectItemList) {
            Map<String, Object> map = new HashMap<>();
            map.put("projectItemDto", projectItemDto);

            List<FundingProjectItemOptionDto> itemOptionList = fundingSqlMapper.findProjectItemOptionByItemId(projectItemDto.getId());
            map.put("itemOptionList", itemOptionList);

            result.add(map);
        }

        return result;
    }
    // 아이템 : 선택한 목록
    public List<Map<String, Object>> getSelectItemDataList(int[] idList){
        List<Map<String, Object>> result = new ArrayList<>();
        List<FundingProjectItemDto> projectItemList = fundingSqlMapper.findProjectItemById(idList);
        for(FundingProjectItemDto projectItemDto : projectItemList) {
            Map<String, Object> map = new HashMap<>();
            map.put("projectItemDto", projectItemDto);

            List<FundingProjectItemOptionDto> itemOptionList = fundingSqlMapper.findProjectItemOptionByItemId(projectItemDto.getId());
            map.put("itemOptionList", itemOptionList);

            result.add(map);
        }

        return result;
    }
    // 아이템 : 개수 - 프로젝트 별
    public int getProjectItemCountByProjectId(int project_id) {
        return fundingSqlMapper.findProjectItemCountByProjectId(project_id);
    }
    // 아이템 : 삭제
    public void deleteProjectItemData(int id) {
        // 아이템
        fundingSqlMapper.deleteProjectItem(id);
        // 옵션
        fundingSqlMapper.deleteProjectItemOptionByItemId(id);
        // 선물 아이템
        fundingSqlMapper.deleteProjectRewardItemByItemId(id);
    }

    // 아이템 옵션 : 생성
    public void registerProjectItemOption(int itemId, String[] options) {
        for(String option : options) {
            FundingProjectItemOptionDto fundingProjectItemOptionDto = new FundingProjectItemOptionDto();
            fundingProjectItemOptionDto.setItem_id(itemId);
            fundingProjectItemOptionDto.setOption_name(option);
            fundingSqlMapper.createProjectItemOptionDto(fundingProjectItemOptionDto);
        }
    }

    // 선물 : 생성
    public void registerProjectReward(FundingProjectRewardDto fundingProjectRewardDto) {
        fundingSqlMapper.createProjectRewardDto(fundingProjectRewardDto);
    }
    // 선물 : 목록 - 프로젝트별
    public List<Map<String, Object>> getProjectRewardDataList(int project_id) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<FundingProjectRewardDto> projectRewardList = fundingSqlMapper.findProjectRewardByProjectId(project_id);

        for(FundingProjectRewardDto projectRewardDto : projectRewardList) {
            Map<String, Object> map = new HashMap<>();

            // 선물 정보
            map.put("projectRewardDto", projectRewardDto);

            // 선물 판매 개수
            int rewardSupportQty = fundingSqlMapper.findSupportRewardQuantityByRewardId(projectRewardDto.getId());
            map.put("rewardSupportQty", rewardSupportQty);

            // 선물 아이템 목록
            List<Map<String, Object>> projectItemDataList = new ArrayList<>();
            List<FundingProjectItemDto> projectItemList = fundingSqlMapper.findProjectItemByRewardId(projectRewardDto.getId());
            for(FundingProjectItemDto projectItemDto : projectItemList) {
                Map<String, Object> mapItem = new HashMap<>();
                // 선물 아이템
                mapItem.put("projectItemDto", projectItemDto);

                // 아이템 옵션
                List<FundingProjectItemOptionDto> itemOptionList = fundingSqlMapper.findProjectItemOptionByItemId(projectItemDto.getId());
                mapItem.put("itemOptionList", itemOptionList);
                projectItemDataList.add(mapItem);
            }
            map.put("projectItemDataList", projectItemDataList);

            result.add(map);
        }

        return result;
    }
    // 선물 : 개수 - 프로젝트 별
    public int getProjectRewardCountByProjectId(int project_id) {
        return fundingSqlMapper.findProjectRewardCountByProjectId(project_id);
    }

    // 하나의선물 정보 : 선택한 아이템, 옵션 데이터 가져오기 - 상품 상세 선택 선물 활용
    public Map<String, Object> getSelectedProjectRewardData(int id, int[] optionIds) {
        Map<String, Object> map = new HashMap<>();

        // 선물 정보
        FundingProjectRewardDto projectRewardDto = fundingSqlMapper.findProjectRewardDto(id);
        map.put("projectRewardDto", projectRewardDto);

        List<Map<String, Object>> projectItemDataList = new ArrayList<>();
        List<FundingProjectItemDto> projectItemList = fundingSqlMapper.findProjectItemByRewardId(projectRewardDto.getId());
        for(FundingProjectItemDto projectItemDto : projectItemList) {
            Map<String, Object> mapItem = new HashMap<>();
            // 선물 아이템
            mapItem.put("projectItemDto", projectItemDto); // 선물에 포함된 모든 아이템

            // 옵션 유무 확인 : 존재할 경우 옵션 정보 추가
            boolean isItemOtion = fundingSqlMapper.findProjectItemOptionCountByItemId(projectItemDto.getId()) > 0;
            if(isItemOtion) {
                FundingProjectItemOptionDto projectItemOption = fundingSqlMapper.findProjectItemOptionByItemIdAndInId(projectItemDto.getId(), optionIds);
                mapItem.put("projectItemOption", projectItemOption);
            }
            projectItemDataList.add(mapItem);
        }
        map.put("projectItemDataList", projectItemDataList);

        return map;
    }

    // 프로젝트의 후원 선물 리스트 - supportId 로
    public List<Map<String, Object>> getSupportProjectRewardDataList(int support_id) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<Map<String, Object>> supportRewardList = fundingSqlMapper.findSupportRewardList(support_id); // pk 포함으로 변경
        for(Map<String, Object> projectRewardDto : supportRewardList) {
            Map<String, Object> map = new HashMap<>();

            // 수량 넣기 : pk 로 가져오도록 수정
            FundingSupportRewardDto supportRewardDto = fundingSqlMapper.findSupportRewardDto((int)projectRewardDto.get("support_reward_id"));
            map.put("supportRewardDto", supportRewardDto);

            // 예상 전달일 : 어차피 선물은 같아서 수정 안함
            Map<String, Object> rewardExpectedDate = fundingSqlMapper.findProjectRewardAndExpectedDate((int)projectRewardDto.get("id"));
            map.put("rewardExpectedDate", rewardExpectedDate);

            // 선물 정보 : map 으로 변경
            map.put("projectRewardDto", projectRewardDto);
            
            // 후원한 아이템, 옵션 목록
            List<Map<String, Object>> projectItemDataList = new ArrayList<>();
            List<FundingProjectItemDto> projectItemList = fundingSqlMapper.findProjectItemByRewardId((int)projectRewardDto.get("id"));
            for(FundingProjectItemDto projectItemDto : projectItemList) {
                Map<String, Object> mapItem = new HashMap<>();
                // 아이템 정보
                mapItem.put("projectItemDto", projectItemDto);

                // 옵션 유무 확인 : 존재할 경우 옵션 정보 추가
                boolean isItemOtion = fundingSqlMapper.findProjectItemOptionCountByItemId(projectItemDto.getId()) > 0;
                if(isItemOtion) {
                    ProjectPledgeItemOptionDto projectItemOption = fundingSqlMapper.findProjectItemOptionByItemIdAndSupportRewardId(projectItemDto.getId(), supportRewardDto.getId());
                    mapItem.put("projectItemOption", projectItemOption);
                }else{
                    mapItem.put("projectItemOption", null);
                }
                projectItemDataList.add(mapItem);
            }
            map.put("projectItemDataList", projectItemDataList);

            result.add(map);
        }
        return result;
    }

    // 프로젝트의 후원 선물 리스트 - Data 로 가져오는거.. : 선택한 선물, 아이템, 옵션 데이터 가져오기 + 주관식은 따로 => local
    public List<Map<String, Object>> getSelectedProjectRewardDataList(int projectId, List<ProjectPledgeDto> pledgeDtoList) {
        
        List<Map<String, Object>> result = new ArrayList<>();

        for(ProjectPledgeDto pledgeDto : pledgeDtoList) {
            int rewardId = pledgeDto.getReward_id();

            Map<String, Object> map = new HashMap<>();
            
            // 선택한 선물 정보
            FundingProjectRewardDto projectRewardDto = fundingSqlMapper.findProjectRewardDto(rewardId);
            map.put("projectRewardDto", projectRewardDto);

            // 선택한 아이템, 옵션
            List<Map<String, Object>> projectItemDataList = new ArrayList<>();
            List<FundingProjectItemDto> projectItemList = fundingSqlMapper.findProjectItemByRewardId(rewardId);
            int optionIndext = 0;
            for(FundingProjectItemDto projectItemDto : projectItemList) {
                Map<String, Object> mapItem = new HashMap<>();
                // 선물 아이템
                mapItem.put("projectItemDto", projectItemDto); // 선물에 포함된 모든 아이템 !!
    
                // 옵션 유무 확인 : 존재할 경우 옵션 정보 추가
                boolean isItemOtion = fundingSqlMapper.findProjectItemOptionCountByItemId(projectItemDto.getId()) > 0;
                if(isItemOtion) {
                    if(projectItemDto.getOption_type().equals("1")) { // 주관식 = 작성 내용 넣기
                        mapItem.put("projectItemContent", pledgeDto.getOption_content()[optionIndext]); // 옵션 순서에 맞는 index로
                    }else if(projectItemDto.getOption_type().equals("2")){ // 객관식
                        FundingProjectItemOptionDto projectItemOption = fundingSqlMapper.findProjectItemOptionByItemIdAndInId(projectItemDto.getId(), pledgeDto.getOption_id());
                        mapItem.put("projectItemOption", projectItemOption);
                    }
                    optionIndext++; // 옵션이 있으면 증가
                }
                projectItemDataList.add(mapItem);
            }
            map.put("projectItemDataList", projectItemDataList);

            // 수량 넣기
            map.put("quantity", pledgeDto.getQuantity());

            // 예상 전달일
            Map<String, Object> rewardExpectedDate = fundingSqlMapper.findProjectRewardAndExpectedDate(rewardId);
            map.put("rewardExpectedDate", rewardExpectedDate);

            result.add(map);
        }

        return result;
    }
    
    // 선물 : 삭제
    public void deleteProjectRewardData(int id) {
        // 선물
        fundingSqlMapper.deleteProjectReward(id);
        // 선물 아이템
        fundingSqlMapper.deleteProjectRewardItemByRewardId(id);
    }

    // 선물 아이템 : 생성
    public void registerProjectRewardItem(int rewardId, int[] itemIdList) {
        for(int itemId : itemIdList) {
            FundingProjectRewardItemDto fundingProjectRewardItemDto = new FundingProjectRewardItemDto();
            fundingProjectRewardItemDto.setReward_id(rewardId);
            fundingProjectRewardItemDto.setItem_id(itemId);
            fundingSqlMapper.createProjectRewardItemDto(fundingProjectRewardItemDto);
        }
    }

    // 후원 : 후원하기
    public void registerFundingSupport(FundingSupportDto fundingSupportDto) {
        fundingSqlMapper.createFundingSupport(fundingSupportDto);
    }
    // 후원 : 선택한 선물
    public void registerFundingSupportReward(FundingSupportRewardDto fundingSupportRewardDto) {
        fundingSqlMapper.createFundingSupportReward(fundingSupportRewardDto);
    }
    // 후원 : 아이템 옵션
    public void registerFundingSupportItemOption(FundingSupportItemOptionDto fundingSupportItemOptionDto) {
        fundingSqlMapper.createFundingSupportItemOption(fundingSupportItemOptionDto);
    }
    // 후원 : 선택한 선물 + 아이템 옵션
    public void registerFundingSupportRewardAndItemOption(int supportId, int projectId, List<ProjectPledgeDto> pledgeDtoList) {
        // 선물 insert
        for(ProjectPledgeDto projectPledge : pledgeDtoList) {
            FundingSupportRewardDto fundingSupportRewardDto = new FundingSupportRewardDto();
            fundingSupportRewardDto.setSupport_id(supportId);
            fundingSupportRewardDto.setReward_id(projectPledge.getReward_id());
            fundingSupportRewardDto.setQuantity(projectPledge.getQuantity());
            fundingSqlMapper.createFundingSupportReward(fundingSupportRewardDto);

            int supportRewardId = fundingSupportRewardDto.getId();

            // 아이템 옵션 값이 있으면
            if(projectPledge.getOption_id().length != 0 && projectPledge.getOption_id() != null){
                int optionIndex = 0;
                for(int optionId : projectPledge.getOption_id()) {
                    FundingSupportItemOptionDto fundingSupportItemOptionDto = new FundingSupportItemOptionDto();
                    fundingSupportItemOptionDto.setSupport_reward_id(supportRewardId);
                    fundingSupportItemOptionDto.setOption_id(optionId);
                    fundingSupportItemOptionDto.setOption_content(projectPledge.getOption_content()[optionIndex]);
                    fundingSqlMapper.createFundingSupportItemOption(fundingSupportItemOptionDto);

                    optionIndex++;
                }
            }
        }
    }
    // 후원 : 실 후원(주문) 진행
    public void pledgeFundingSupport(int id, String status, String pay_status) {
        fundingSqlMapper.pledgeFundingSupport(id, status, pay_status);
    }
    // 후원 : 배송 데이터
    public void registerFundingSupportDelivery(FundingSupportDeliveryDto fundingSupportDeliveryDto) {
        fundingSqlMapper.createFundingSupportDelivery(fundingSupportDeliveryDto);
    }

    // 후원 : 결제 금액 계산
    public int getSupportTotalPrice(int support_id) {
        return fundingSqlMapper.findSupportTotalPrice(support_id);
    }
    // 후원 : 후원 완료시 후원자 수
    public int getBackersCountBySupportId(int support_id) {
        return fundingSqlMapper.findBackersCountBySupportId(support_id);
    }
    // 후원 id 로 프로젝트 id 찾기
    public int getProjectIdBySupportId(int support_id) {
        return fundingSqlMapper.findProjectIdBySupportId(support_id);
    }

    // 후원 : 정보 출력
    public Map<String, Object> getSupportData(int id) {
        Map<String, Object> map = new HashMap<>();
        
        // 후원 정보
        FundingSupportDto supportDto = fundingSqlMapper.findFundingSupport(id);
        map.put("supportDto", supportDto);

        boolean isDelivery = fundingSqlMapper.findIsDeliveryCountBySupportReward(id) > 0;
        // 배송 상품이 있는 경우에만
        if(isDelivery) {
            FundingSupportDeliveryDto supportDeliveryDto = fundingSqlMapper.findFundingSupportDeliveryBySupportId(id);
            map.put("supportDeliveryDto", supportDeliveryDto);
        }else{
            map.put("supportDeliveryDto", null);
        }

        // 결제 정보

        return map;
    }
    // 후원 : 회원별 목록 출력
    public List<Map<String, Object>> getSupportDataList(int user_id, String status) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<SupportDataDto> supportList = fundingSqlMapper.findFundingSupportListByUserId(user_id, status);
        for(SupportDataDto supportData : supportList) {
            int supportId = supportData.getId();

            Map<String, Object> map = new HashMap<>();
            // 후원+프로젝트 정보
            map.put("supportData", supportData);

            // 선물 
            List<Map<String, Object>> supportRewardDataList = new ArrayList<>();
            List<Map<String, Object>> supportRewardList = fundingSqlMapper.findSupportRewardList(supportId);
            for(Map<String, Object> projectRewardDto : supportRewardList) {
                Map<String, Object> mapReward = new HashMap<>();
                // 선물 정보
                mapReward.put("projectRewardDto", projectRewardDto);
                // 수량 넣기
                FundingSupportRewardDto supportRewardDto = fundingSqlMapper.findSupportRewardDto((int)projectRewardDto.get("support_reward_id"));
                mapReward.put("supportRewardDto", supportRewardDto);

                supportRewardDataList.add(mapReward);
            }
            map.put("rewardDataList", supportRewardDataList);

            // 결제 금액 정보
            int supportTotalPrice = fundingSqlMapper.findSupportTotalPrice(supportId);
            map.put("totalPrice", supportTotalPrice);

            // 후기 작성 여부
            boolean isReviewWrite = fundingSqlMapper.findProjectReviewCountBySupportId(supportId) > 0;
            map.put("isReviewWrite", isReviewWrite);

            result.add(map);
        }

        return result;
    }
    // 후원 : 목록 개수
    public int getSupportDataListCount(int user_id) {
        return fundingSqlMapper.findFudingSupportListCountByUserId(user_id);
    }
    // 후원자 목록
    public List<Map<String, Object>> getBackersListByProjectUserId(int user_id, int project_id) {
        return fundingSqlMapper.findBackersListByProjectUserId(user_id, project_id);
    }
    public int getBackersListByProjectCountUserId(int user_id, int project_id) {
        return fundingSqlMapper.findBackersListCountByProjectUserId(user_id, project_id);
    }
    // 선물 배송처리
    public void updateSupportDeliveryStatus(int id, String delivery_status) {
        fundingSqlMapper.updateSupportDeliveryStatus(id, delivery_status);
    }
    // 해당 프로젝트 후원 유무 확인
    public boolean getSupportCountByProjectIdAndUserId(int project_id, int user_id) {
        return fundingSqlMapper.findSupportCountByProjectIdAndUserId(project_id, user_id) > 0;
    }
    // 해당 프로젝트 후원 id
    public int getSupportIdByProjectIdAndUserId(int project_id, int user_id) {
        return fundingSqlMapper.findSupportIdByProjectIdAndUserId(project_id, user_id);
    }
    
    // 후기 : 전체 키워드 리스트
    public List<FundingProjectReviewKeywordDto> getProjectReviewKeywordList() {
        return fundingSqlMapper.findProjectReviewKeywordList();
    }
    // 후기 작성 : 후기, 키워드, 파일
    public void registerReviewData(FundingProjectReviewDto projectReviewDto, int[] reviewKeywords, List<ImageDto> imageDtoList) {
        // 리뷰 작성
        fundingSqlMapper.createProjectReview(projectReviewDto);
        
        // 키워드 작성
        for(int keyword : reviewKeywords) {
            FundingProjectReviewSelectedKeywordDto reviewSelectedKeywordDto = new FundingProjectReviewSelectedKeywordDto();
            reviewSelectedKeywordDto.setReview_id(projectReviewDto.getId());
            reviewSelectedKeywordDto.setKeyword_id(keyword);
            fundingSqlMapper.createProjectReviewSelectedKeyword(reviewSelectedKeywordDto);
        }

        // 파일 작성
        for(ImageDto image : imageDtoList) {
            if(image == null) continue;
            FundingProjectReviewFileDto reviewFileDto = new FundingProjectReviewFileDto();
            reviewFileDto.setReview_id(projectReviewDto.getId());
            reviewFileDto.setLocation(image.getLocation());
            fundingSqlMapper.createProjectReviewFile(reviewFileDto);
        }

        // GPT 요약 작성
        boolean reviewGptCount = fundingSqlMapper.findProjectReviewGptCountByReviewId(projectReviewDto.getId()) > 0;
        // 요약글이 없는 경우에만 추가
        if(!reviewGptCount) {
            String summaryContent = projectReviewDto.getContent();
            // 10 글자 미만 제외
            if(summaryContent.length() < 10) {
                summaryContent = "GPT요약없음";
            }else{
                summaryContent = openAiGptService.summarizeContent(summaryContent);
            }
            // DB 저장 처리
            FundingProjectReviewGptDto projectReviewGptDto = new FundingProjectReviewGptDto();
            projectReviewGptDto.setReview_id(projectReviewDto.getId());
            projectReviewGptDto.setGpt_content(summaryContent);
            fundingSqlMapper.createProjectReviewGpt(projectReviewGptDto);
        }
    }

    // 후기 작성 여부 확인
    public boolean isProjectReviewWrite(int support_id) {
        return fundingSqlMapper.findProjectReviewCountBySupportId(support_id) > 0;
    }
    // 후기 목록 : 프로젝트 별
    public List<Map<String, Object>> getProjectReviewDataListByProjectId(int project_id) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<FundingProjectReviewDto> projectReviewList = fundingSqlMapper.findProjectReviewByProjectId(project_id);
        for(FundingProjectReviewDto projectReview : projectReviewList) {
            Map<String, Object> map = new HashMap<>();
            // 후기 정보
            map.put("projectReview", projectReview);
            
            // 후기 작성 유저
            int writerUserId = fundingSqlMapper.findSupportUserId(projectReview.getSupport_id()); // 후기 작성 userid
            UserDto userDto = userSqlMapper.userIdInfo(writerUserId);
            map.put("userDto", userDto);

            // 후기 키워드
            List<Map<String, Object>> selectedKeywordList = fundingSqlMapper.findProjectReviewSelectedKeywordByReviewId(projectReview.getId());
            map.put("selectedKeywordList", selectedKeywordList);

            // 후기 첨부파일
            List<FundingProjectReviewFileDto> reviewFileList = fundingSqlMapper.findProjectReviewFileByReviewId(projectReview.getId());
            map.put("reviewFileList", reviewFileList);

            // GPT 요약
            FundingProjectReviewGptDto projectReviewGpt = fundingSqlMapper.findProjectReviewGptByReviewId(projectReview.getId());
            if(!projectReviewGpt.getGpt_content().equals("GPT요약없음")) {
                map.put("projectReviewGpt", projectReviewGpt);
            }

            result.add(map);
        }
        return result;
    }
    // 후기 목록 개수
    public int getProjectReviewDataListCountByProjectId(int project_id) {
        return fundingSqlMapper.findProjectReviewCountByProjectId(project_id);
    }

    // 커뮤니티 : 작성
    public void registerProjectCommnunity(FundingProjectCommunityDto projectCommunityDto) {
        fundingSqlMapper.createProjectCommunity(projectCommunityDto);
    }
    // 커뮤니티 : 목록
    public List<Map<String, Object>> getProjectCommunityDataListByProjectId(int project_id) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<FundingProjectCommunityDto> projectCommunityList = fundingSqlMapper.findProjectCommunityListByProjectId(project_id);
        for(FundingProjectCommunityDto projectCommunityDto : projectCommunityList) {
            Map<String, Object> map = new HashMap<>();
            // 커뮤니티 정보
            map.put("projectCommunityDto", projectCommunityDto);

            // 유저정보
            UserDto userDto = userSqlMapper.userIdInfo(projectCommunityDto.getUser_id());
            map.put("userDto", userDto);

            // 댓글 정보
            List<Map<String, Object>> communityCommentDataList = new ArrayList<>();
            List<FundingProjectCommunityCommentDto> communityCommentList = fundingSqlMapper.findCommunityCommentListByCommunityId(projectCommunityDto.getId());
            for(FundingProjectCommunityCommentDto commentDto : communityCommentList) {
                Map<String, Object> newMap = new HashMap<>();
                // 댓글 정보
                newMap.put("commentDto", commentDto);
                // 댓글 유저 >> 창작자
                FundingProjectCreatorDto creatorDto = fundingSqlMapper.findProjectCreatorDto(commentDto.getUser_id());
                newMap.put("creatorDto", creatorDto);

                communityCommentDataList.add(newMap);
            }
            map.put("commentDataList", communityCommentDataList);

            result.add(map);
        }
        return result;
    }
    // 커뮤니티 목록 개수
    public int getProjectCommunityDataListCountByProjectId(int project_id) {
        return fundingSqlMapper.findProjectCommunityListCountByProjectId(project_id);
    }
    // 커뮤니티 댓글 작성
    public void registerProjectCommnunityComment(FundingProjectCommunityCommentDto projectCommunityCommentDto) {
        fundingSqlMapper.createProjectCommunityComment(projectCommunityCommentDto);
    }

    // 업데이트 : 작성
    public void registerProjectUpdate(FundingProjectUpdateDto projectUpdateDto) {
        fundingSqlMapper.createProjectUpdate(projectUpdateDto);
    }
    // 업데이트 : 글목록 - 프로젝트 별
    public List<Map<String, Object>> getProjectUpdateDataListByProjectId(int project_id) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<FundingProjectUpdateDto> projectUpdateList = fundingSqlMapper.findProjectUpdateListByProjectId(project_id);
        for(FundingProjectUpdateDto projectUpdateDto : projectUpdateList) {
            Map<String, Object> map = new HashMap<>();
            // 업데이트 정보
            map.put("projectUpdateDto", projectUpdateDto);

            // 유저정보
            FundingProjectCreatorDto creatorDto = fundingSqlMapper.findProjectCreatorDto(projectUpdateDto.getUser_id());
            map.put("creatorDto", creatorDto);

            // 댓글 정보
            int commentCount = fundingSqlMapper.findProjectUpdateCommentListCountByUpdateId(projectUpdateDto.getId());
            map.put("commentCount", commentCount);

            result.add(map);
        }
        return result;
    }
    // 업데이트 : 글목록 - 개수
    public int getProjectUpdateListCountByProjectId(int project_id) {
        return fundingSqlMapper.findProjectUpdateListCountByProjectId(project_id);
    }
    // 업데이트 : 게시물 단일
    public Map<String, Object> getProjectUpdateData(int id) {
        Map<String, Object> map = new HashMap<>();

        // 업데이트 게시물
        FundingProjectUpdateDto projectUpdateDto = fundingSqlMapper.findProjectUpdateDto(id);
        map.put("projectUpdateDto", projectUpdateDto);
        // 창작자
        FundingProjectCreatorDto projectCreatorDto = fundingSqlMapper.findProjectCreatorDto(projectUpdateDto.getUser_id());
        map.put("projectCreatorDto", projectCreatorDto);

        return map;
    }
    // 업데이트 : 댓글 작성
    public void registerProjectUpdateComment(FundingProjectUpdateCommentDto projectUpdateCommentDto){
        fundingSqlMapper.createProjectUpdateComment(projectUpdateCommentDto);
    }
    // 업데이트 : 게시물의 댓글 목록
    public List<Map<String, Object>> getProjectUpdateCommentList(int update_id) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<FundingProjectUpdateCommentDto> updateCommentList = fundingSqlMapper.findProjectUpdateCommentListByUpdateId(update_id);
        for(FundingProjectUpdateCommentDto updateComment : updateCommentList) {
            Map<String, Object> map = new HashMap<>();
            // 댓글 정보
            map.put("updateComment", updateComment);
            // 작성자
            UserDto userDto = userSqlMapper.userIdInfo(updateComment.getUser_id());
            map.put("userDto", userDto);
            result.add(map);
        }
        return result;
    }
    // 업데이트 : 게시물 댓글 개수
    public int getProjectUpdateCommentListCount(int update_id) {
        return fundingSqlMapper.findProjectUpdateCommentListCountByUpdateId(update_id);
    }

    // 상품 목록 : 메인용 - 최신, 마감, 랜덤
    public List<Map<String, Object>> getProjectDataListForMain(String sortBy) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<ProjectListDataDto> projectDataList = fundingSqlMapper.findProjectDataListForMain(sortBy);
        for(ProjectListDataDto projectData : projectDataList) {
            Map<String, Object> map = new HashMap<>();
            // 프로젝트 정보
            map.put("projectData", projectData);

            // 후원 비율
            ProjectPledgeStatusDto pledgeStatus = fundingSqlMapper.findProjectPledgeStatusByProjectId(projectData.getId());
            map.put("pledgeStatus", pledgeStatus);

            result.add(map);
        }

        return result;
    }
    // 상품 목록 : 메인용 - 인기
    public List<Map<String, Object>> getPorpularProjectDataListForMain() {
        List<Map<String, Object>> result = new ArrayList<>();
        List<Map<String, Object>> projectDataList = fundingSqlMapper.findPopularProjectDataListForMain();
        for(Map<String, Object> projectData : projectDataList) {
            Map<String, Object> map = new HashMap<>();
            // 프로젝트 정보
            map.put("projectData", projectData);

            // 후원 비율
            ProjectPledgeStatusDto pledgeStatus = fundingSqlMapper.findProjectPledgeStatusByProjectId((int)projectData.get("project_id"));
            map.put("pledgeStatus", pledgeStatus);

            result.add(map);
        }

        return result;
    }

    // 프로젝트 관리 : 대시보드 - 일일 후원금
    public List<Map<String, Object>> getDailyAmountByProjectId(int project_id) {
        return fundingSqlMapper.findDailyAmountByProjectId(project_id);
    }
    // 프로젝트 관리 : 대시보드 - 후원자 성별 비율
    public List<Map<String, Object>> getSupportCountByGender(int project_id) {
        return fundingSqlMapper.findSupportCountByGender(project_id);
    }
    // 프로젝트 관리 : 대시보드 - 후원자 나이대 비율
    public List<Map<String, Object>> getSupportCountByAgeGroup(int project_id) {
        return fundingSqlMapper.findSupportCountByAgeGroup(project_id);
    }

    // 관리자용 : 프로젝트 목록
    public List<Map<String, Object>> getAdminProjectList(
        int pageIndex, Integer categoryId, String fromStartDate, String toStartDate, 
        String[] statusList, String fromEndDate, String toEndDate, String searchOption, String searchWord
    ) {
        return fundingSqlMapper.findAdminProjectList((pageIndex-1)*10, categoryId, fromStartDate, toStartDate, statusList, fromEndDate, toEndDate, searchOption, searchWord);
    }
    public int getAdminProjectListCount(
        Integer categoryId, String fromStartDate, String toStartDate, 
        String[] statusList, String fromEndDate, String toEndDate, String searchOption, String searchWord
    ) {
        return fundingSqlMapper.findAdminProjectListCount(categoryId, fromStartDate, toStartDate, statusList, fromEndDate, toEndDate, searchOption, searchWord); // 여기도 동일하게 추가하기
    }

    // 심사 : 작성
    public void registerApprovalHistory(FundingApprovalHistory fundingApprovalHistory) {
        fundingSqlMapper.createApprovalHistory(fundingApprovalHistory);
    }
    // 심사 : 목록
    public List<FundingApprovalHistory> getApprovalHistoryByProjectId(int project_id) {
        return fundingSqlMapper.findApprovalHistoryByProjectId(project_id);
    }

    // 관리자용 : 후원자 목록
    public List<Map<String, Object>> getAdminBackersList(int pageIndex) {
        return fundingSqlMapper.findAdminBackersList((pageIndex-1)*10);
    }
    public int getAdminBackersListCount() {
        return fundingSqlMapper.findAdminBackersListCount();
    }

    // 공용 관리자 대시보드 : 상태 개수
    public Map<String, Object> getFundingProjectStatusCount() {
        return fundingSqlMapper.findProjectStatusCount();
    }

    // 정산 내역 목록
    public List<Map<String, Object>> getSettlementDetailsList(int pageIndex) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<FundingSettlementDetailsDto> settlementDetailsList = fundingSqlMapper.findSettlementDetailsList((pageIndex-1)*10);
        for(FundingSettlementDetailsDto settlementDetails : settlementDetailsList) {
            Map<String, Object> map = new HashMap<>();
            // 정산 내역
            map.put("settlementDetails", settlementDetails);
            // 프로젝트 달성 금액
            int projectTotalAmount = fundingSqlMapper.findProjectTotalAmountByProjectId(settlementDetails.getProject_id());
            map.put("projectTotalAmount", projectTotalAmount);
            // 정산 예정일
            Date createdAt = settlementDetails.getCreated_at();
            LocalDate localDate = createdAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate expectedDate = localDate.plusDays(7);
            map.put("expectedDate", expectedDate);

            result.add(map);
        }

        return result;
    }
    public int getSettlementDetailsListCount() {
        return fundingSqlMapper.findSettlementDetailListCount();
    }
    public boolean isSettlementDetailCount(int project_id) {
        return fundingSqlMapper.findSettlementDetailCountByProjectId(project_id) > 0;
    }
    // 정산내역 - 단일
    public Map<String, Object> getSettlementDetails(int id){ 
        Map<String, Object> map = new HashMap<>();

        // 정산 정보
        FundingSettlementDetailsDto settlementDetails = fundingSqlMapper.findSettlementDetails(id);
        map.put("settlementDetails", settlementDetails);
        // 프로젝트 달성 금액
        int projectTotalAmount = fundingSqlMapper.findProjectTotalAmountByProjectId(settlementDetails.getProject_id());
        map.put("projectTotalAmount", projectTotalAmount);
        // 정산 예정일
        Date createdAt = settlementDetails.getCreated_at();
        LocalDate localDate = createdAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate expectedDate = localDate.plusDays(7);
        map.put("expectedDate", expectedDate);
    
        return map;
    }
    // 정산내역 - 단일 : project_id 로 가져오기
    public FundingSettlementDetailsDto getSettlementDetailsByProjectId(int project_id) {
        return fundingSqlMapper.findSettlementDetailsByProjectId(project_id);
    }
    // 정산내역 : 상태 변경
    public void updateSettlementStatus(FundingSettlementDetailsDto settlementDetailsDto) {
        fundingSqlMapper.updateSettlementStatus(settlementDetailsDto);
    }
    // 정산내역 : 후원 내역 가져오기
    public List<Map<String, Object>> getSettlementBackersList(int project_id) {
        return fundingSqlMapper.findSettlementBackersList(project_id);
    }
    public int getSettlementBackersListCount(int project_id) {
        return fundingSqlMapper.findSettlementBackersListCount(project_id);
    }


    // 카카오페이 결제 관련
    public RequestKakaoPayDto getRequestKakaoPayDto(int support_id) {
        return fundingSqlMapper.findRequestKakaoPayDto(support_id);
    }
    // 카카오페이 DB 저장
    public void registerFundingPayment(FundingPaymentDto fundingPaymentDto) {
        fundingSqlMapper.createFundingPayment(fundingPaymentDto);
    }
}