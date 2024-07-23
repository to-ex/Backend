package com.example.toex.engTest.service;

import com.example.toex.engTest.dto.EngTest;
import com.example.toex.engTest.dto.enums.*;
import com.example.toex.engTest.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@RequiredArgsConstructor
@Service
public class EngTestService {


    @Autowired
    private final TestRepository testRepository;


    public List<EngTest> getTests(String category, String area, String type, LocalDate date) {
        if (area == null && type == null) {
            return testRepository.findByTestCategoryAndTestDate(category, date);
        } else if (area == null) {
            return testRepository.findByTestCategoryAndTestTypeAndTestDate(category, type, date);
        } else if (type == null) {
            return testRepository.findByTestCategoryAndTestAreaAndTestDate(category, area, date);
        } else {
            return testRepository.findByTestCategoryAndTestAreaAndTestTypeAndTestDate(category, area, type, date);
        }
    }


    public List<EngTest> getTestsByCategory(String category) {
        return testRepository.findByTestCategory(category);
    }

}