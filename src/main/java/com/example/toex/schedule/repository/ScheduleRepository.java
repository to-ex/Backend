package com.example.toex.schedule.repository;

import com.example.toex.schedule.domain.Schedule;
import com.example.toex.schedule.domain.enums.ScheduleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByUserId(Long userId);

    List<Schedule> findByUserIdAndType(Long userId, ScheduleType type);
}