package com.sixthband.festival.information.dto;


import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class FestivalLineUpDto {
    private int id;
    private int performance_id;//등록된 performance_date의 PK로 생각하면 됨
    private int artist_id;
}
