package com.example.toex.schedule.service;

import com.example.toex.schedule.dto.Schedule;
import com.example.toex.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public Schedule createSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public Schedule updateSchedule(Long scheduleId, Schedule schedule) {
        Schedule existingSchedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found"));

        Schedule updatedSchedule = Schedule.builder()
                .scheduleId(existingSchedule.getScheduleId())
                .userId(existingSchedule.getUserId())
                .scheduleCategory(schedule.getScheduleCategory())
                .content(schedule.getContent())
                .isDone(schedule.getIsDone())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .build();

        return scheduleRepository.save(updatedSchedule);
    }

    public void deleteSchedule(Long scheduleId) {
        Schedule existingSchedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found"));
        scheduleRepository.delete(existingSchedule);
    }


    public Schedule toggleScheduleDone(Long scheduleId) {
        Schedule existingSchedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found"));

        Schedule updatedSchedule = Schedule.builder()
                .scheduleId(existingSchedule.getScheduleId())
                .userId(existingSchedule.getUserId())
                .scheduleCategory(existingSchedule.getScheduleCategory())
                .content(existingSchedule.getContent())
                .isDone(!existingSchedule.getIsDone())  // 현재 상태를 반전시킴
                .startDate(existingSchedule.getStartDate())
                .endDate(existingSchedule.getEndDate())
                .build();

        return scheduleRepository.save(updatedSchedule);
    }

    public List<Schedule> getSchedulesByUserId(Long userId) {
        return scheduleRepository.findByUserId(userId);
    }
}