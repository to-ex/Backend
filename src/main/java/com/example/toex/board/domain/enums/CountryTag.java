package com.example.toex.board.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CountryTag {
    SPAIN("SPAIN", "스페인"),
    GERMANY("GERMANY", "독일"),
    ENGLAND("ENGLAND", "영국"),
    FRANCE("FRANCE", "프랑스"),
    ITALY("ITALY", "이탈리아"),
    ;


    private final String code;
    private final String name;
}
