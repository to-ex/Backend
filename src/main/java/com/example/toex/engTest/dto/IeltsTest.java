package com.example.toex.engTest.dto;

import com.example.toex.engTest.domain.enums.IeltsArea;
import com.example.toex.engTest.domain.enums.IeltsType;
import com.example.toex.engTest.domain.enums.TestCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IeltsTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long testId;

    @Enumerated(EnumType.STRING)
    private TestCategory testCategory;

    @Enumerated(EnumType.STRING)
    private IeltsArea ieltsArea;

    @Column(nullable = false)
    private String testPlaceAddress;

    @Column(nullable = false)
    private String testPlaceName;

    @Column(nullable = false)
    private LocalDateTime testDateTime;

    @Enumerated(EnumType.STRING)
    private IeltsType ieltsType;
}