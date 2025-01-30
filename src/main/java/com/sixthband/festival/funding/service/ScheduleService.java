package com.sixthband.festival.funding.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sixthband.festival.funding.dto.FundingProjectDto;
import com.sixthband.festival.funding.dto.FundingScheduleLogDto;
import com.sixthband.festival.funding.dto.FundingSettlementDetailsDto;
import com.sixthband.festival.funding.dto.FundingSupportDto;
import com.sixthband.festival.funding.mapper.FundingSqlMapper;

@Service
public class ScheduleService {

    @Autowired
    private FundingSqlMapper fundingSqlMapper;

    // 프로젝트 상태
    public void updateProjectAndSupportStatus() {

        LocalDate today = LocalDate.now(); // 현재 날짜
        LocalDateTime now = LocalDateTime.now(); // 현재 시간
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX); // 오늘 23:59:59

        // 종료 처리 : 진행중인 프로젝트 리스트
        List<FundingProjectDto> ongoingProjectList = fundingSqlMapper.findOngoingProjectList("진행중");
        for(FundingProjectDto projectDto : ongoingProjectList) {
            int projectId = projectDto.getId();

            String oldStatus = projectDto.getStatus(); // 기존 상태
            // 프로젝트 마감시간
            Date endAt = projectDto.getEnd_at();
            LocalDate endAtDate = endAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            // 마감 시간이 어제인지 확인 : 전날 23:59:59가 포함된 마감
            if(endAtDate.isEqual(today.minusDays(1))) {
                int totalAmount = fundingSqlMapper.findProjectTotalAmountByProjectId(projectId);
                boolean isSuccess = totalAmount >= projectDto.getExpect_amount();
                String resultStatus = (isSuccess) ? "성공" : "무산"; // 프로젝트 결과
                String payCancel = "결제취소"; // 결제취소

                // 프로젝트 상태 업데이트
                fundingSqlMapper.updateProjectStatus(projectId, resultStatus);

                // 후원 상태 업데이트
                List<FundingSupportDto> supportDtoList = fundingSqlMapper.findSupportListByProjectId(projectId);
                for(FundingSupportDto supportDto : supportDtoList) {
                    if(!isSuccess) { // 후원 실패시
                        // 카카오 결제 취소 처리도 되어야 함

                        // 후원 상태 무산
                        fundingSqlMapper.updateSupportStatus(supportDto.getId(), resultStatus, payCancel);
                    }
                }

                // 정산 내역 업데이트 : 성공인 경우
                if(isSuccess) {
                    // 정산 금액 계산 : 고정 소수점 연산
                    long totalAmountLong = (long)totalAmount * 100; // 고정 소수점 연산을 위한 *100
                    long settlementFeeLong = totalAmountLong * 8 / 100; // 수수료 8%
                    long settlementAmountLong = totalAmountLong - settlementFeeLong;
                    // 정수로 변환
                    int settlementFee = (int) (settlementFeeLong / 100);
                    int settlementAmount = (int) (settlementAmountLong / 100);

                    FundingSettlementDetailsDto settlementDetails = new FundingSettlementDetailsDto();
                    settlementDetails.setProject_id(projectId);
                    settlementDetails.setSettlement_fee(settlementFee); // 수수료 (8%)
                    settlementDetails.setSettlement_amount(settlementAmount); // 실정산 금액
                    // 정산 내역 업데이트
                    fundingSqlMapper.createSettlementDetails(settlementDetails);
                }

                // 프로젝트 상태 변경 로그 남기기
                FundingScheduleLogDto fundingScheduleLogDto = new FundingScheduleLogDto();
                fundingScheduleLogDto.setProject_id(projectId);
                fundingScheduleLogDto.setOld_status(oldStatus);
                fundingScheduleLogDto.setNew_status(resultStatus);
                fundingSqlMapper.createFundingScheduleLog(fundingScheduleLogDto);
            }
        }

        // 진행 처리 : 승인완료 프로젝트
        List<FundingProjectDto> approvalProjectList = fundingSqlMapper.findOngoingProjectList("승인");
        for(FundingProjectDto projectDto : approvalProjectList) {
            int projectId = projectDto.getId();

            String oldStatus = projectDto.getStatus(); // 기존 상태
            // 프로젝트 시작시간을 LocalDataTime 으로 변환
            Date startAt = projectDto.getStart_at();
            LocalDate startAtDate = startAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(); // 형변환 - 날짜만 포함

            // 시작 시간이 오늘 날짜보다 이전인지 확인 (과거)
            if (startAtDate.isBefore(today) || startAtDate.isEqual(today.minusDays(1))) {
                // 프로젝트 상태 업데이트 : 진행중
                String resultStatus = "진행중";
                fundingSqlMapper.updateProjectStatus(projectId, resultStatus);

                // 프로젝트 상태 변경 로그 남기기
                FundingScheduleLogDto fundingScheduleLogDto = new FundingScheduleLogDto();
                fundingScheduleLogDto.setProject_id(projectId);
                fundingScheduleLogDto.setOld_status(oldStatus);
                fundingScheduleLogDto.setNew_status(resultStatus);
                fundingSqlMapper.createFundingScheduleLog(fundingScheduleLogDto);
            }
        }

        System.out.println(">>>>> Funding ScheduleServie 실행 <<<<<");
    }
}
