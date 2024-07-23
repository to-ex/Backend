package com.example.toex.engTest.controller;

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
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        if (date == null) {
            date = LocalDate.now();
        }

        List<EngTest> tests = engTestService.getTests(category, area, type, date);


        System.out.println("tests = " + tests);
        return ResponseEntity.ok(tests);

    }

    @GetMapping("getAll")
    public ResponseEntity<List<EngTest>> getTestsByCategory(@RequestParam String category
    ){
        List<EngTest> tests = engTestService.getTestsByCategory(category);
        return ResponseEntity.ok(tests);
    }

}