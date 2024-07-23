package com.example.toex.engTest.service;

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
        if (date == null) {
            date = String.valueOf(LocalDate.now());
        }

        if (area == null && type == null) {
            return testRepository.findByTestCategoryAndDate(category, date);
        } else {
            return testRepository.findByTestCategoryAndTestAreaAndTestTypeAndTestDate(category, area, type, date);
        }
    }

    public List<EngTest> getTestsByCategory(String category) {
        return testRepository.findByTestCategory(category);
    }
}
