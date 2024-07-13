package com.example.toex.board.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLikes is a Querydsl query type for Likes
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLikes extends EntityPathBase<Likes> {

    private static final long serialVersionUID = 159527049L;

    public static final QLikes likes = new QLikes("likes");

    public final com.example.toex.common.QBaseEntity _super = new com.example.toex.common.QBaseEntity(this);

    public final NumberPath<Long> boardId = createNumber("boardId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDt = _super.createdDt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedDt = _super.deletedDt;

    //inherited
    public final StringPath delYn = _super.delYn;

    public final NumberPath<Long> likeId = createNumber("likeId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedDt = _super.updatedDt;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QLikes(String variable) {
        super(Likes.class, forVariable(variable));
    }

    public QLikes(Path<? extends Likes> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLikes(PathMetadata metadata) {
        super(Likes.class, metadata);
    }

}

