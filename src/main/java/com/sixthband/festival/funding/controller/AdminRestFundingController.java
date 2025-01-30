package com.sixthband.festival.funding.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sixthband.festival.dto.RestResponseDto;
import com.sixthband.festival.funding.dto.FundingApprovalHistory;
import com.sixthband.festival.funding.dto.FundingSettlementDetailsDto;
import com.sixthband.festival.funding.service.FundingService;

@RestController
@RequestMapping("api/admin/funding")
public class AdminRestFundingController {
    @Autowired
    private FundingService fundingService;

    // 프로젝트 관리 : 심사 결과 반영
    @RequestMapping("approvalStatus")
    public RestResponseDto approvalStatus(FundingApprovalHistory params, @RequestParam("mail_subject") String mail_subject, @RequestParam("mail_content") String mail_content) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        
        // 심사 로그
        fundingService.registerApprovalHistory(params);

        // 프로젝트 단계변경
        fundingService.updateProjectStatus(params.getProject_id(), params.getResult());

        // 메일 발송 처리 필요

        return restResponseDto;
    }
    
    // 프로젝트 관리 : 심사 결과 출력
    @RequestMapping("approvalHistory")
    public RestResponseDto approvalHistory(@RequestParam("project_id") int project_id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        List<FundingApprovalHistory> approvalHistory = fundingService.getApprovalHistoryByProjectId(project_id);
        restResponseDto.add("approvalHistory", approvalHistory);

        return restResponseDto;
    }

    // 정산 관리 : 상태 변경
    @RequestMapping("updateSettlementStatus")
    public RestResponseDto settlementStatusProcess(FundingSettlementDetailsDto params) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        // 정산 처리
        fundingService.updateSettlementStatus(params);

        // 정산 정보
        Map<String, Object> settlementDto = fundingService.getSettlementDetails(params.getId());
        restResponseDto.add("settlementDto", settlementDto);

        return restResponseDto;
    }
}
