package com.example.toex.schedule.service;

import com.example.toex.engTest.domain.EngTest;
import com.example.toex.engTest.repository.TestRepository;
import com.example.toex.schedule.domain.Schedule;
import com.example.toex.schedule.domain.enums.ScheduleCategory;
import com.example.toex.schedule.domain.enums.ScheduleType;
import com.example.toex.schedule.dto.res.ScheduleRes;
import com.example.toex.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final TestRepository testRepository;

    public ScheduleRes createSchedule(Schedule schedule) {
        // 날짜가 설정된 경우 캘린더로 설정, 그렇지 않으면 체크리스트로 설정
        if (schedule.getStartDate() == null || schedule.getEndDate() == null) {
            schedule.setType(ScheduleType.CHECKLIST);
        } else {
            schedule.setType(ScheduleType.CALENDAR);
        }
        Schedule savedSchedule = scheduleRepository.save(schedule);
        return toDto(savedSchedule);
    }

    public ScheduleRes updateSchedule(Long scheduleId, Schedule schedule) {
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

        Schedule updatedSchedule = scheduleRepository.save(existingSchedule);
        return toDto(updatedSchedule);
    }

    public void deleteSchedule(Long scheduleId) {
        Schedule existingSchedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found"));
        scheduleRepository.delete(existingSchedule);
    }

    public ScheduleRes toggleScheduleDone(Long scheduleId) {
        Schedule existingSchedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found"));

        existingSchedule.setIsDone(!existingSchedule.getIsDone());
        Schedule updatedSchedule = scheduleRepository.save(existingSchedule);
        return toDto(updatedSchedule);
    }

    public List<ScheduleRes> getSchedulesByUserIdAndType(Long userId, ScheduleType type) {
        List<Schedule> schedules;
        if (type == null) {
            schedules = scheduleRepository.findByUserId(userId);
        } else {
            schedules = scheduleRepository.findByUserIdAndType(userId, type);
        }
        return schedules.stream().map(this::toDto).collect(Collectors.toList());
    }

    public ScheduleRes createScheduleFromEngTest(Long testId, Long userId) {
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

        Schedule savedSchedule = scheduleRepository.save(schedule);
        return toDto(savedSchedule);
    }

    private ScheduleRes toDto(Schedule schedule) {
        return ScheduleRes.builder()
                .scheduleId(schedule.getScheduleId())
                .scheduleCategory(schedule.getScheduleCategory())
                .content(schedule.getContent())
                .isDone(schedule.getIsDone())
                .userId(schedule.getUserId())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .type(schedule.getType())
                .build();
    }
}
