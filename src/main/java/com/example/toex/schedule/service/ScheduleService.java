package com.example.toex.schedule.service;

import com.example.toex.common.exception.CustomException;
import com.example.toex.common.exception.enums.ErrorCode;
import com.example.toex.engTest.dto.EngTest;
import com.example.toex.engTest.repository.TestRepository;
import com.example.toex.schedule.domain.Schedule;

import com.example.toex.schedule.domain.ScheduleCategory;
import com.example.toex.schedule.domain.ScheduleType;
import com.example.toex.schedule.dto.ScheduleDTO;
import com.example.toex.schedule.repository.ScheduleRepository;
import com.example.toex.security.CustomUserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.toex.common.exception.enums.ErrorCode.INVALID_SCHEDULE;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final TestRepository testRepository;

    public Schedule createSchedule(ScheduleDTO scheduleDTO, CustomUserDetail userDetail) {
        Long userId = getUserId(userDetail, true);

        Schedule schedule = Schedule.builder()
                .userId(userId)
                .scheduleCategory(scheduleDTO.getScheduleCategory())
                .content(scheduleDTO.getContent())
                .isDone(scheduleDTO.getIsDone())
                .startDate(scheduleDTO.getStartDate())
                .endDate(scheduleDTO.getEndDate())
                .type(scheduleDTO.getType())
                .build();

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
                .orElseThrow(() -> new CustomException(INVALID_SCHEDULE));

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
                .orElseThrow(() -> new CustomException(INVALID_SCHEDULE));
        //이미 삭제되었을 경우
        if( existingSchedule.getDelYn().equals("Y")){
            throw new CustomException(ErrorCode.ALREADY_DELETE_SCHEDULE);
        }
        existingSchedule.delete(); // 소프트 삭제
        scheduleRepository.save(existingSchedule);
    }

    public Schedule toggleScheduleDone(Long scheduleId) {
        Schedule existingSchedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CustomException(INVALID_SCHEDULE));

        existingSchedule.setIsDone(!existingSchedule.getIsDone());
        return scheduleRepository.save(existingSchedule);
    }

    public List<Schedule> getSchedulesByUserIdAndType(ScheduleType type, CustomUserDetail userDetail) {
        Long userId = getUserId(userDetail, true);
        if (type == null) {
            return scheduleRepository.findByUserId(userId);
        } else {
            return scheduleRepository.findByUserIdAndType(userId, type);
        }
    }

    public Schedule createScheduleFromEngTest(Long testId, CustomUserDetail userDetail) {
        EngTest engTest = testRepository.findById(testId)
                .orElseThrow(() -> new CustomException(INVALID_SCHEDULE));

        Long userId = getUserId(userDetail, true);
        Schedule schedule = Schedule.builder()
                .userId(userId)
                .scheduleCategory(ScheduleCategory.TEST)
                .content(engTest.getTestCategory())
                .startDate(engTest.getTestDateTime().toLocalDate())
                .endDate(engTest.getTestDateTime().toLocalDate())
                .type(ScheduleType.CALENDAR)
                .isDone(false)  // 명시적으로 isDone 설정
                .build();

        return scheduleRepository.save(schedule);
    }

    public Long getUserId(CustomUserDetail userDetail, Boolean authcheck) {
        if (userDetail == null || userDetail.getUser() == null) {
            if (authcheck) {
                throw new CustomException(ErrorCode.INVALID_TOKEN);
            }
            return null;
        }
        return userDetail.getUser().getUserId();
    }
}
