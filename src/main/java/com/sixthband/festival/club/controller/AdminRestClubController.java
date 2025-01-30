package com.sixthband.festival.club.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sixthband.festival.club.service.ClubService;
import com.sixthband.festival.dto.RestResponseDto;

@RestController
@RequestMapping("api/admin")
public class AdminRestClubController {

    @Autowired
    private ClubService clubService;

    @RequestMapping("boardReportList")
    public RestResponseDto boardReportList(){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("boardReportList", clubService.boardReportList());

        return restResponseDto;

    }
    
    @RequestMapping("reportDetailPage")
    public RestResponseDto boardReportDetail(@RequestParam("id")int id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("reportDetailPage", clubService.boardReportDetail(id));

        return restResponseDto;

    }

     // 신고 게시판 삭제 
    @RequestMapping("deleteContent")
    public RestResponseDto deleteContent(@RequestParam("id")int id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        clubService.deleteBoardAdminProcess(id);

        return restResponseDto;

    }
    
}
