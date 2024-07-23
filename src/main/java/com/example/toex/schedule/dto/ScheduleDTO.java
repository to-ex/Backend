package com.example.toex.schedule.dto;

import com.example.toex.schedule.domain.ScheduleCategory;
import com.example.toex.schedule.domain.ScheduleType;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;

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

    private Long userId;

    @Builder
    public ScheduleDTO(ScheduleCategory scheduleCategory, String content, Boolean isDone, LocalDate startDate, LocalDate endDate, ScheduleType type) {
        this.scheduleCategory = scheduleCategory;
        this.content = content;
        this.isDone = isDone;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
    }
}
