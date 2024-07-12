package com.example.toex.schedule.controller;

import com.example.toex.schedule.dto.Schedule;

import com.example.toex.schedule.dto.ScheduleType;
import com.example.toex.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping
    public ResponseEntity<List<Schedule>> getSchedules(@RequestParam Long userId, @RequestParam(required = false) ScheduleType type) {
        List<Schedule> schedules = scheduleService.getSchedulesByUserIdAndType(userId, type);
        return ResponseEntity.ok(schedules);
    }
    //스케줄 생성
    @PostMapping
    public ResponseEntity<Schedule> createSchedule(@RequestBody Schedule schedule) {
        Schedule createdSchedule = scheduleService.createSchedule(schedule);
        return ResponseEntity.ok(createdSchedule);
    }

    // 어학 시험 일정 스케줄 추가
    @PostMapping("/engTest/{testId}")
    public ResponseEntity<Schedule> createScheduleFromEngTest(@PathVariable Long testId, @RequestParam Long userId) {
        Schedule createdSchedule = scheduleService.createScheduleFromEngTest(testId, userId);
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
