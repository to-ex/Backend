package com.example.toex.board.domain;

import com.example.toex.board.domain.enums.BoardCategory;
import com.example.toex.common.BaseEntity;
import com.example.toex.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@DynamicUpdate
@Getter
@NoArgsConstructor
@Entity(name = "board")
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    private String title;

    @Enumerated(EnumType.STRING)
    private BoardCategory boardCategory;

    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardImg> boardImgs = new ArrayList<>();
}
