package com.example.toex.schedule.dto;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSchedule is a Querydsl query type for Schedule
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSchedule extends EntityPathBase<Schedule> {

    private static final long serialVersionUID = 56178926L;

    public static final QSchedule schedule = new QSchedule("schedule");

    public final StringPath content = createString("content");

    public final DatePath<java.time.LocalDate> endDate = createDate("endDate", java.time.LocalDate.class);

    public final BooleanPath isDone = createBoolean("isDone");

    public final EnumPath<ScheduleCategory> scheduleCategory = createEnum("scheduleCategory", ScheduleCategory.class);

    public final NumberPath<Long> scheduleId = createNumber("scheduleId", Long.class);

    public final DatePath<java.time.LocalDate> startDate = createDate("startDate", java.time.LocalDate.class);

    public final EnumPath<ScheduleType> type = createEnum("type", ScheduleType.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QSchedule(String variable) {
        super(Schedule.class, forVariable(variable));
    }

    public QSchedule(Path<? extends Schedule> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSchedule(PathMetadata metadata) {
        super(Schedule.class, metadata);
    }

}

