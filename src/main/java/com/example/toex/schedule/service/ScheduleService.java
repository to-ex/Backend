package com.example.toex.schedule.service;

import com.example.toex.engTest.dto.EngTest;
import com.example.toex.engTest.repository.TestRepository;
import com.example.toex.schedule.dto.Schedule;

import com.example.toex.schedule.dto.ScheduleCategory;
import com.example.toex.schedule.dto.ScheduleType;
import com.example.toex.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final TestRepository testRepository;

    public Schedule createSchedule(Schedule schedule) {
        // 날짜가 설정된 경우 캘린더로 설정, 그렇지 않으면 체크리스트로 설정
        if (schedule.getStartDate() == null || schedule.getEndDate() == null) {
            schedule.setType(ScheduleType.CHECKLIST);
        } else {
            schedule.setType(ScheduleType.CALENDAR);
        }
        return scheduleRepository.save(schedule);
    }

    public Schedule updateSchedule(Long scheduleId, Schedule schedule) {
        Schedule existingSchedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found"));

        // 날짜가 설정된 경우 캘린더로 설정, 그렇지 않으면 체크리스트로 설정
        if (schedule.getStartDate() == null || schedule.getEndDate() == null) {
            schedule.setType(ScheduleType.CHECKLIST);
        } else {
            schedule.setType(ScheduleType.CALENDAR);
        }

        existingSchedule.setScheduleCategory(schedule.getScheduleCategory());
        existingSchedule.setContent(schedule.getContent());
        existingSchedule.setIsDone(schedule.getIsDone());
        existingSchedule.setStartDate(schedule.getStartDate());
        existingSchedule.setEndDate(schedule.getEndDate());
        existingSchedule.setType(schedule.getType());

        return scheduleRepository.save(existingSchedule);
    }

    public void deleteSchedule(Long scheduleId) {
        Schedule existingSchedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found"));
        scheduleRepository.delete(existingSchedule);
    }

    public Schedule toggleScheduleDone(Long scheduleId) {
        Schedule existingSchedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found"));

        existingSchedule.setIsDone(!existingSchedule.getIsDone());
        return scheduleRepository.save(existingSchedule);
    }

    public List<Schedule> getSchedulesByUserIdAndType(Long userId, ScheduleType type) {
        if (type == null) {
            return scheduleRepository.findByUserId(userId);
        } else {
            return scheduleRepository.findByUserIdAndType(userId, type);
        }
    }

    public Schedule createScheduleFromEngTest(Long testId, Long userId) {
        EngTest engTest = testRepository.findById(testId)
                .orElseThrow(() -> new IllegalArgumentException("Test not found"));

        Schedule schedule = Schedule.builder()
                .userId(userId)
                .scheduleCategory(ScheduleCategory.TEST)
                .content("English Test: " + engTest.getTestType() + " in " + engTest.getTestArea())
                .startDate(engTest.getTestDateTime().toLocalDate())
                .endDate(engTest.getTestDateTime().toLocalDate())
                .type(ScheduleType.CALENDAR)
                .isDone(false)  // 명시적으로 isDone 설정
                .build();

        return scheduleRepository.save(schedule);
    }
}
