package com.example.toex.schedule.dto;

import com.example.toex.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "Schedule")
@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Schedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScheduleCategory scheduleCategory;

    @Column
    @Builder.Default
    private String content = "";

    @Column(nullable = false)
    private Boolean isDone;

    @Column(nullable = false)
    private Long userId;

    @Column
    @Builder.Default
    private LocalDate startDate = LocalDate.now();


    @Column
    @Builder.Default
    private LocalDate endDate = LocalDate.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScheduleType type;


}

