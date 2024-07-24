package com.example.toex.schedule.dto;

import com.example.toex.schedule.domain.Schedule;
import com.example.toex.schedule.domain.ScheduleCategory;
import com.example.toex.schedule.domain.ScheduleType;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDTO {

    private Long scheduleId;
    private ScheduleCategory scheduleCategory;
    private String content;
    private Boolean isDone;
    private LocalDate startDate;
    private LocalDate endDate;
    private ScheduleType type;
    private LocalDateTime createdDt;
    private LocalDateTime updatedDt;

    private Long userId;

    @Builder
    public ScheduleDTO(Schedule schedule,ScheduleCategory scheduleCategory, String content, Boolean isDone, LocalDate startDate, LocalDate endDate, ScheduleType type) {
        this.scheduleCategory = scheduleCategory;
        this.content = content;
        this.isDone = isDone;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        this.createdDt = schedule.getCreatedDt();
        this.updatedDt = schedule.getUpdatedDt();
    }
}
