package com.sixthband.festival.funding.dto;

import java.util.List;

import lombok.Data;

@Data
public class ProjectPledgeDataDto {
    private int projectId;
    private List<ProjectPledgeDto> pledgeDtoList;
}
