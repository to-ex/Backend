package com.example.toex.engTest.repository;

import com.example.toex.engTest.dto.EngTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<EngTest, Long> {

        @Query(value = "SELECT * FROM eng_test WHERE test_category = :testCategory AND test_area = :testArea AND test_type = :testType AND test_date = :testDate", nativeQuery = true)
        List<EngTest> findByTestCategoryAndTestAreaAndTestTypeAndTestDate(
                @Param("testCategory") String testCategory,
                @Param("testArea") String testArea,
                @Param("testType") String testType,
                @Param("testDate") String testDate
        );

        @Query("SELECT e FROM EngTest e WHERE e.testCategory = :testCategory AND e.testDate = :testDate")
        List<EngTest> findTestsByCategoryAndDate(@Param("testCategory") String testCategory, @Param("testDate") String testDate);

        @Query(value = "SELECT * FROM eng_test WHERE test_category = :testCategory AND test_type = :testType AND test_date = :testDate", nativeQuery = true)
        List<EngTest> findByTestCategoryAndTestTypeAndTestDate(
                @Param("testCategory") String testCategory,
                @Param("testType") String testType,
                @Param("testDate") String testDate
        );

        @Query(value = "SELECT * FROM eng_test WHERE test_category = :testCategory AND test_area = :testArea AND test_date = :testDate", nativeQuery = true)
        List<EngTest> findByTestCategoryAndTestAreaAndTestDate(
                @Param("testCategory") String testCategory,
                @Param("testArea") String testArea,
                @Param("testDate") String testDate
        );

        @Query(value = "SELECT * FROM eng_test WHERE test_category = :testCategory", nativeQuery = true)
        List<EngTest> findByTestCategory(@Param("testCategory") String testCategory);
}
