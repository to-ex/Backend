package com.example.toex.schedule.controller;

import com.example.toex.schedule.dto.Schedule;
import com.example.toex.schedule.repository.ScheduleRepository;
import com.example.toex.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/schedule")
public class ScheduleController {

    private final ScheduleRepository scheduleRepository;
    private final ScheduleService scheduleService;

    //스케줄 조회
    @GetMapping
    public ResponseEntity<List<Schedule>> getSchedules(@RequestParam Long userId) {
        List<Schedule> schedules = scheduleRepository.findByUserId(userId);
        return ResponseEntity.ok(schedules);
    }

    //스케줄 생성
    @PostMapping
    public ResponseEntity<Schedule> createSchedule(@RequestBody Schedule schedule) {
        Schedule createdSchedule = scheduleService.createSchedule(schedule);
        return ResponseEntity.ok(createdSchedule);
    }

    @PatchMapping("/{scheduleId}")
    public ResponseEntity<Schedule> updateSchedule(@PathVariable Long scheduleId, @RequestBody Schedule schedule) {
        Schedule updatedSchedule = scheduleService.updateSchedule(scheduleId, schedule);
        return ResponseEntity.ok(updatedSchedule);
    }


    // 스케줄 삭제
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long scheduleId) {
        scheduleService.deleteSchedule(scheduleId);
        return ResponseEntity.noContent().build();
    }

    // 스케줄 완료 상태 토글
    @PatchMapping("/done/{scheduleId}")
    public ResponseEntity<Schedule> toggleScheduleDone(@PathVariable Long scheduleId) {
        Schedule updatedSchedule = scheduleService.toggleScheduleDone(scheduleId);
        return ResponseEntity.ok(updatedSchedule);
    }


}
