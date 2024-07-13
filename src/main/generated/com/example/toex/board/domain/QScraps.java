package com.example.toex.board.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QScraps is a Querydsl query type for Scraps
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QScraps extends EntityPathBase<Scraps> {

    private static final long serialVersionUID = 845438869L;

    public static final QScraps scraps = new QScraps("scraps");

    public final com.example.toex.common.QBaseEntity _super = new com.example.toex.common.QBaseEntity(this);

    public final NumberPath<Long> boardId = createNumber("boardId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDt = _super.createdDt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedDt = _super.deletedDt;

    //inherited
    public final StringPath delYn = _super.delYn;

    public final NumberPath<Long> scrapId = createNumber("scrapId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedDt = _super.updatedDt;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QScraps(String variable) {
        super(Scraps.class, forVariable(variable));
    }

    public QScraps(Path<? extends Scraps> path) {
        super(path.getType(), path.getMetadata());
    }

    public QScraps(PathMetadata metadata) {
        super(Scraps.class, metadata);
    }

}

