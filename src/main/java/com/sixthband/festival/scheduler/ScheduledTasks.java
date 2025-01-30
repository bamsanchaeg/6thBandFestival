package com.sixthband.festival.scheduler;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sixthband.festival.funding.service.ScheduleService;

@Component
public class ScheduledTasks {

    @Autowired
    private ScheduleService scheduleService;

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행 // 밤이면 59 59 23
    public void fundingStatusTask() {
        System.out.println("작업 실행시간 : " + LocalDateTime.now());

        // 프로젝트 상태 변경 서비스 실행
        scheduleService.updateProjectAndSupportStatus();
    }
}
