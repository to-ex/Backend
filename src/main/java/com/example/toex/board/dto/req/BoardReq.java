package com.example.toex.board.dto.req;

import com.example.toex.board.domain.enums.BoardCategory;
import com.example.toex.board.domain.enums.CountryTag;
import lombok.Getter;

@Getter
public class BoardReq {
    private String title;
    private BoardCategory boardCategory;
    private CountryTag countryTag;
    private String content;
}
