package com.example.toex.engTest.controller;

import com.example.toex.engTest.domain.enums.TestArea;
import com.example.toex.engTest.domain.enums.TestCategory;
import com.example.toex.engTest.domain.enums.TestType;
import com.example.toex.engTest.domain.EngTest;
import com.example.toex.engTest.service.EngTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/engTest")
public class EngTestController {

    private final EngTestService engTestService;

    @GetMapping
    public ResponseEntity<List<EngTest>> getTests(
            @RequestParam TestCategory category,
            @RequestParam(required = false) TestArea area,
            @RequestParam(required = false) TestType type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        if (date == null) {
            date = LocalDate.now();
        }

        List<EngTest> tests = engTestService.getTests(category, area, type, date);

        System.out.println("tests = " + tests);
        return ResponseEntity.ok(tests);
    }
}