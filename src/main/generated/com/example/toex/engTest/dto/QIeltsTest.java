package com.example.toex.engTest.dto;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QIeltsTest is a Querydsl query type for IeltsTest
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QIeltsTest extends EntityPathBase<IeltsTest> {

    private static final long serialVersionUID = -11657403L;

    public static final QIeltsTest ieltsTest = new QIeltsTest("ieltsTest");

    public final EnumPath<com.example.toex.engTest.dto.enums.IeltsArea> ieltsArea = createEnum("ieltsArea", com.example.toex.engTest.dto.enums.IeltsArea.class);

    public final EnumPath<com.example.toex.engTest.dto.enums.IeltsType> ieltsType = createEnum("ieltsType", com.example.toex.engTest.dto.enums.IeltsType.class);

    public final EnumPath<com.example.toex.engTest.dto.enums.TestCategory> testCategory = createEnum("testCategory", com.example.toex.engTest.dto.enums.TestCategory.class);

    public final DateTimePath<java.time.LocalDateTime> testDateTime = createDateTime("testDateTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> testId = createNumber("testId", Long.class);

    public final StringPath testPlaceAddress = createString("testPlaceAddress");

    public final StringPath testPlaceName = createString("testPlaceName");

    public QIeltsTest(String variable) {
        super(IeltsTest.class, forVariable(variable));
    }

    public QIeltsTest(Path<? extends IeltsTest> path) {
        super(path.getType(), path.getMetadata());
    }

    public QIeltsTest(PathMetadata metadata) {
        super(IeltsTest.class, metadata);
    }

}

