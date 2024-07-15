package com.example.toex.schedule.dto.res;

import com.example.toex.schedule.domain.enums.ScheduleCategory;
import com.example.toex.schedule.domain.enums.ScheduleType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@Builder
public class ScheduleRes {

    private Long scheduleId;

    private ScheduleCategory scheduleCategory;

    private String content;

    private Boolean isDone;

    private Long userId;

    @Builder.Default
    private LocalDate startDate = LocalDate.now();

    @Builder.Default
    private LocalDate endDate = LocalDate.now();

    private ScheduleType type;
}
