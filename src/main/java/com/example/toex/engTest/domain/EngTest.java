package com.example.toex.engTest.domain;

import com.example.toex.engTest.domain.enums.TestArea;
import com.example.toex.engTest.domain.enums.TestCategory;
import com.example.toex.engTest.domain.enums.TestType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "eng_test")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EngTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long testId;

    @Enumerated(EnumType.STRING)
    @Column(name = "test_category", nullable = false)
    private TestCategory testCategory;

    @Enumerated(EnumType.STRING)
    @Column(name = "test_area", nullable = false)
    private TestArea testArea;

    @Column(name = "test_place_address", nullable = false)
    private String testPlaceAddress;

    @Column(name = "test_place_name", nullable = false)
    private String testPlaceName;

    @Column(name = "test_date_time", nullable = false)
    private LocalDateTime testDateTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "test_type", nullable = false)
    private TestType testType;
}
