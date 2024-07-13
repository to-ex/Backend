package com.example.toex.engTest.dto;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QToeflTest is a Querydsl query type for ToeflTest
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QToeflTest extends EntityPathBase<ToeflTest> {

    private static final long serialVersionUID = -495417082L;

    public static final QToeflTest toeflTest = new QToeflTest("toeflTest");

    public final DateTimePath<java.time.LocalDateTime> testDateTime = createDateTime("testDateTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> testId = createNumber("testId", Long.class);

    public final StringPath testPlaceAddress = createString("testPlaceAddress");

    public final StringPath testPlaceName = createString("testPlaceName");

    public final EnumPath<com.example.toex.engTest.dto.enums.ToeflArea> toeflArea = createEnum("toeflArea", com.example.toex.engTest.dto.enums.ToeflArea.class);

    public final EnumPath<com.example.toex.engTest.dto.enums.ToeflType> toeflType = createEnum("toeflType", com.example.toex.engTest.dto.enums.ToeflType.class);

    public QToeflTest(String variable) {
        super(ToeflTest.class, forVariable(variable));
    }

    public QToeflTest(Path<? extends ToeflTest> path) {
        super(path.getType(), path.getMetadata());
    }

    public QToeflTest(PathMetadata metadata) {
        super(ToeflTest.class, metadata);
    }

}

