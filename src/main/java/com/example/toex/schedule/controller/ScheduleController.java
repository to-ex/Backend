package com.example.toex.schedule.controller;

import com.example.toex.common.message.BasicResponse;
import com.example.toex.schedule.domain.Schedule;
import com.example.toex.schedule.domain.enums.ScheduleType;
import com.example.toex.schedule.dto.res.ScheduleRes;
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
    public ResponseEntity<BasicResponse<List<ScheduleRes>>> getSchedules(@RequestParam Long userId, @RequestParam(required = false) ScheduleType type) {
        List<ScheduleRes> schedules = scheduleService.getSchedulesByUserIdAndType(userId, type);
        return ResponseEntity.ok(BasicResponse.ofSuccess(schedules));
    }

    // 스케줄 생성
    @PostMapping
    public ResponseEntity<BasicResponse<ScheduleRes>> createSchedule(@RequestBody Schedule schedule) {
        ScheduleRes createdSchedule = scheduleService.createSchedule(schedule);
        return ResponseEntity.ok(BasicResponse.ofSuccess(createdSchedule));
    }

    // 어학 시험 일정 스케줄 추가
    @PostMapping("/engTest/{testId}")
    public ResponseEntity<BasicResponse<ScheduleRes>> createScheduleFromEngTest(@PathVariable Long testId, @RequestParam Long userId) {
        ScheduleRes createdSchedule = scheduleService.createScheduleFromEngTest(testId, userId);
        return ResponseEntity.ok(BasicResponse.ofSuccess(createdSchedule));
    }

    // 스케줄 수정
    @PatchMapping("/{scheduleId}")
    public ResponseEntity<BasicResponse<ScheduleRes>> updateSchedule(@PathVariable Long scheduleId, @RequestBody Schedule schedule) {
        ScheduleRes updatedSchedule = scheduleService.updateSchedule(scheduleId, schedule);
        return ResponseEntity.ok(BasicResponse.ofSuccess(updatedSchedule));
    }

    // 스케줄 삭제
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<BasicResponse<Void>> deleteSchedule(@PathVariable Long scheduleId) {
        scheduleService.deleteSchedule(scheduleId);
        return ResponseEntity.ok(BasicResponse.ofSuccess(null));
    }

    @PatchMapping("/done/{scheduleId}")
    public ResponseEntity<BasicResponse<ScheduleRes>> toggleScheduleDone(@PathVariable Long scheduleId) {
        ScheduleRes updatedSchedule = scheduleService.toggleScheduleDone(scheduleId);
        return ResponseEntity.ok(BasicResponse.ofSuccess(updatedSchedule));
    }
}
