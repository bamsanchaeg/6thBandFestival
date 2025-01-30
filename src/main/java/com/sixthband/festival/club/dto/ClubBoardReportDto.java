package com.sixthband.festival.club.dto;

import lombok.Data;
import java.util.Date;

@Data
public class ClubBoardReportDto {
    private int id;
    private int club_id;
    private int club_board_id;
    private int club_member_id;
    private String report_reason;
    private Date created_at;
}
