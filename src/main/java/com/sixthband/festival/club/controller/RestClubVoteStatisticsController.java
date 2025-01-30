package com.sixthband.festival.club.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sixthband.festival.club.dto.MySelectVoteDto;
import com.sixthband.festival.club.service.ClubService;
import com.sixthband.festival.dto.RestResponseDto;

@RestController
@RequestMapping("api/vote")
public class RestClubVoteStatisticsController {
    @Autowired
    private ClubService clubService;
    //그래프
    @RequestMapping("voteStatistics")
    public RestResponseDto voteStatistics(MySelectVoteDto mySelectVoteDto){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("femaleVote", clubService.selectVoteFemale(mySelectVoteDto));
        restResponseDto.add("maleVote", clubService.selectVoteMale(mySelectVoteDto));

        return restResponseDto; 
    }
}
