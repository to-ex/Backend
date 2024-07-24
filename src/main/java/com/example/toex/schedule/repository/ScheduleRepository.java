package com.example.toex.schedule.repository;

import com.example.toex.schedule.domain.Schedule;
import com.example.toex.schedule.domain.ScheduleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
//    List<Schedule> findByUserId(Long userId);
//
//    List<Schedule> findByUserIdAndType(Long userId, ScheduleType type);
//

    @Query("SELECT s FROM Schedule s WHERE s.userId = :userId AND s.delYn = 'N'")
    List<Schedule> findByUserId(Long userId);

    @Query("SELECT s FROM Schedule s WHERE s.userId = :userId AND s.type = :type AND s.delYn = 'N'")
    List<Schedule> findByUserIdAndType(Long userId, ScheduleType type);
}