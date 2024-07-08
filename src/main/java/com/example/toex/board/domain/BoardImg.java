package com.example.toex.board.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@DynamicUpdate
@Getter
@NoArgsConstructor
@Entity(name = "board_img")
public class BoardImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardImgId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    private String imgUrl;
}
