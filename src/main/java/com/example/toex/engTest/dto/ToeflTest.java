package com.example.toex.engTest.dto;

import com.example.toex.engTest.dto.enums.TestCategory;
import com.example.toex.engTest.dto.enums.ToeflArea;
import com.example.toex.engTest.dto.enums.ToeflType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "toefl_test")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ToeflTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long testId;

    @Enumerated(EnumType.STRING)
    private ToeflArea toeflArea;

    @Column(nullable = false)
    private String testPlaceAddress;

    @Column(nullable = false)
    private String testPlaceName;

    @Column(nullable = false)
    private LocalDateTime testDateTime;

    @Enumerated(EnumType.STRING)
    private ToeflType toeflType;
}