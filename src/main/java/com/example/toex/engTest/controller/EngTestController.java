package com.example.toex.engTest.controller;

import com.example.toex.common.exception.CustomException;
import com.example.toex.common.exception.enums.ErrorCode;
import com.example.toex.engTest.dto.EngTest;
import com.example.toex.engTest.dto.enums.*;

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
            @RequestParam String category,
            @RequestParam(required = false) String area,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String date) {

        if (date == null)  {
            date = String.valueOf(LocalDate.now());
        }
        List<EngTest> tests = engTestService.getTests(category, area, type, date);

        return ResponseEntity.ok(tests);

    }
    //필터 없이 전체 조회
    @GetMapping("/getAll")
    public ResponseEntity<List<EngTest>> getTestsByCategory(@RequestParam String category
    ){
        List<EngTest> tests = engTestService.getTestsByCategory(category);
        return ResponseEntity.ok(tests);
    }

}