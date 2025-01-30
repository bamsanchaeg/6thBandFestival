package com.sixthband.festival.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class RestResponseDto {
    private String result;
    private String reason;
    private Map<String, Object> data = new HashMap<>();

    public void add(String name, Object value) {
        data.put(name, value);
    }
}
