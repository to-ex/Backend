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


    public List<EngTest> getTests(TestCategory category, String area, TestType type, LocalDate date) {
        if (area == null && type == null) {
            return testRepository.findByTestCategoryAndTestDate(category.name(), date);
        } else if (area == null) {
            return testRepository.findByTestCategoryAndTestTypeAndTestDate(category.name(), type.name(), date);
        } else if (type == null) {
            return testRepository.findByTestCategoryAndTestAreaAndTestDate(category.name(), area, date);
        } else {
            return testRepository.findByTestCategoryAndTestAreaAndTestTypeAndTestDate(category.name(), area, type.name(), date);
        }
    }

}