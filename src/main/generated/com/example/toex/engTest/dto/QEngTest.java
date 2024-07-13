package com.example.toex.engTest.dto;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QEngTest is a Querydsl query type for EngTest
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEngTest extends EntityPathBase<EngTest> {

    private static final long serialVersionUID = -804095916L;

    public static final QEngTest engTest = new QEngTest("engTest");

    public final EnumPath<com.example.toex.engTest.dto.enums.TestArea> testArea = createEnum("testArea", com.example.toex.engTest.dto.enums.TestArea.class);

    public final EnumPath<com.example.toex.engTest.dto.enums.TestCategory> testCategory = createEnum("testCategory", com.example.toex.engTest.dto.enums.TestCategory.class);

    public final DateTimePath<java.time.LocalDateTime> testDateTime = createDateTime("testDateTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> testId = createNumber("testId", Long.class);

    public final StringPath testPlaceAddress = createString("testPlaceAddress");

    public final StringPath testPlaceName = createString("testPlaceName");

    public final EnumPath<com.example.toex.engTest.dto.enums.TestType> testType = createEnum("testType", com.example.toex.engTest.dto.enums.TestType.class);

    public QEngTest(String variable) {
        super(EngTest.class, forVariable(variable));
    }

    public QEngTest(Path<? extends EngTest> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEngTest(PathMetadata metadata) {
        super(EngTest.class, metadata);
    }

}

