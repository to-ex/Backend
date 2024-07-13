package com.example.toex.board.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoard is a Querydsl query type for Board
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoard extends EntityPathBase<Board> {

    private static final long serialVersionUID = 150461363L;

    public static final QBoard board = new QBoard("board");

    public final com.example.toex.common.QBaseEntity _super = new com.example.toex.common.QBaseEntity(this);

    public final EnumPath<com.example.toex.board.domain.enums.BoardCategory> boardCategory = createEnum("boardCategory", com.example.toex.board.domain.enums.BoardCategory.class);

    public final NumberPath<Long> boardId = createNumber("boardId", Long.class);

    public final ListPath<BoardImg, QBoardImg> boardImgs = this.<BoardImg, QBoardImg>createList("boardImgs", BoardImg.class, QBoardImg.class, PathInits.DIRECT2);

    public final ListPath<Comment, QComment> comments = this.<Comment, QComment>createList("comments", Comment.class, QComment.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    public final EnumPath<com.example.toex.board.domain.enums.CountryTag> countryTag = createEnum("countryTag", com.example.toex.board.domain.enums.CountryTag.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDt = _super.createdDt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedDt = _super.deletedDt;

    //inherited
    public final StringPath delYn = _super.delYn;

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedDt = _super.updatedDt;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QBoard(String variable) {
        super(Board.class, forVariable(variable));
    }

    public QBoard(Path<? extends Board> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBoard(PathMetadata metadata) {
        super(Board.class, metadata);
    }

}

