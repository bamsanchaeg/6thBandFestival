package com.sixthband.festival.bubble.dto;


import lombok.Data;

import java.sql.Timestamp;

@Data
public class BubbleChatDto {

    private int id;
    private int chat_id;
    private int sender;
    private int receiver;
    private String content;
    private int read_or_not;
    private Timestamp joined_at;
}
