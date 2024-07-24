package com.example.toex.engTest.service;

import com.example.toex.common.exception.CustomException;
import com.example.toex.common.exception.enums.ErrorCode;
import com.example.toex.engTest.dto.EngTest;
import com.example.toex.engTest.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class EngTestService {


    @Autowired
    private final TestRepository testRepository;

    public List<EngTest> getTests(String category, String area, String type, String date) {
        List<EngTest> tests;
        System.out.println("date = " + date);
        if (area == null && type == null) {

            tests = testRepository.findTestsByCategoryAndDate(category, date);
        } else if (area == null) {
            tests = testRepository.findByTestCategoryAndTestTypeAndTestDate(category, type, date);
        } else if (type == null) {
            tests = testRepository.findByTestCategoryAndTestAreaAndTestDate(category, area, date);
        } else {
            tests = testRepository.findByTestCategoryAndTestAreaAndTestTypeAndTestDate(category, area, type, date);
        }

        if (tests.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_CONTENT);
        }

        return tests;
    }

    public List<EngTest> getTestsByCategory(String category) {
        return testRepository.findByTestCategory(category);
    }
}
