package com.example.toex.board.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BoardCategory {
    SHARE("SHARE", "공유해요"),
    QUESTION("QUESTION", "질문있어요"),
    TALK("TALK", "떠들어요"),
    ;

    private final String code;
    private final String name;
}
