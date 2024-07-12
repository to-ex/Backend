package com.example.toex.engTest.repository;

import com.example.toex.engTest.dto.EngTest;
import com.example.toex.engTest.dto.enums.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<EngTest, Long> {

        // 필터링 조건: 카테고리, 지역, 타입, 날짜
        @Query(value = "SELECT * FROM eng_test e WHERE e.test_category = :testCategory AND e.test_area = :testArea AND e.test_type = :testType AND DATE(e.test_date_time) = :testDate", nativeQuery = true)
        List<EngTest> findByTestCategoryAndTestAreaAndTestTypeAndTestDate(@Param("testCategory") String testCategory, @Param("testArea") String testArea, @Param("testType") String testType, @Param("testDate") LocalDate testDate);

        // 필터링 조건: 카테고리, 날짜
        @Query(value = "SELECT * FROM eng_test e WHERE e.test_category = :testCategory AND DATE(e.test_date_time) = :testDate", nativeQuery = true)
        List<EngTest> findByTestCategoryAndTestDate(@Param("testCategory") String testCategory, @Param("testDate") LocalDate testDate);

        // 필터링 조건: 카테고리, 타입, 날짜
        @Query(value = "SELECT * FROM eng_test e WHERE e.test_category = :testCategory AND e.test_type = :testType AND DATE(e.test_date_time) = :testDate", nativeQuery = true)
        List<EngTest> findByTestCategoryAndTestTypeAndTestDate(@Param("testCategory") String testCategory, @Param("testType") String testType, @Param("testDate") LocalDate testDate);

        // 필터링 조건: 카테고리, 지역, 날짜
        @Query(value = "SELECT * FROM eng_test e WHERE e.test_category = :testCategory AND e.test_area = :testArea AND DATE(e.test_date_time) = :testDate", nativeQuery = true)
        List<EngTest> findByTestCategoryAndTestAreaAndTestDate(@Param("testCategory") String testCategory, @Param("testArea") String testArea, @Param("testDate") LocalDate testDate);
}