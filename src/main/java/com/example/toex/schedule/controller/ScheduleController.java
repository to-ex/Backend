package com.example.toex.schedule.controller;

import com.example.toex.schedule.domain.Schedule;

import com.example.toex.schedule.domain.ScheduleType;
import com.example.toex.schedule.dto.ScheduleDTO;
import com.example.toex.schedule.service.ScheduleService;
import com.example.toex.security.CustomUserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping
    public ResponseEntity<List<Schedule>> getSchedules(@RequestParam(required = false) ScheduleType type, @AuthenticationPrincipal CustomUserDetail userDetail) {
        List<Schedule> schedules = scheduleService.getSchedulesByUserIdAndType(type,userDetail);
        return ResponseEntity.ok(schedules);
    }
    //스케줄 생성
    @PostMapping
    public ResponseEntity<Schedule> createSchedule(@RequestBody ScheduleDTO schedule, @AuthenticationPrincipal CustomUserDetail userDetail) {
        Schedule createdSchedule = scheduleService.createSchedule(schedule,userDetail);
        return ResponseEntity.ok(createdSchedule);
    }

    // 어학 시험 일정 스케줄 추가
    @PostMapping("/engTest/{testId}")
    public ResponseEntity<Schedule> createScheduleFromEngTest(@PathVariable Long testId,  @AuthenticationPrincipal CustomUserDetail userDetail) {
        Schedule createdSchedule = scheduleService.createScheduleFromEngTest(testId,userDetail);
        return ResponseEntity.ok(createdSchedule);

    }
    // 스케줄 수정
    @PatchMapping("/{scheduleId}")
    public ResponseEntity<Schedule> updateSchedule(@PathVariable Long scheduleId, @RequestBody Schedule schedule) {
        Schedule updatedSchedule = scheduleService.updateSchedule(scheduleId, schedule);
        return ResponseEntity.ok(updatedSchedule);
    }
    //스케줄 삭제
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long scheduleId) {
        scheduleService.deleteSchedule(scheduleId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/done/{scheduleId}")
    public ResponseEntity<Schedule> toggleScheduleDone(@PathVariable Long scheduleId) {
        Schedule updatedSchedule = scheduleService.toggleScheduleDone(scheduleId);
        return ResponseEntity.ok(updatedSchedule);
    }
}
