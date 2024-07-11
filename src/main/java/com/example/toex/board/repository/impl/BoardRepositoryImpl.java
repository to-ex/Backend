package com.example.toex.board.repository.impl;

import com.example.toex.board.domain.enums.BoardCategory;
import com.example.toex.board.domain.enums.CountryTag;
import com.example.toex.board.dto.res.BoardRes;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.toex.board.domain.QBoard.board;
import static com.example.toex.user.QUser.user;
import static com.example.toex.board.domain.QLikes.likes;
import static com.example.toex.board.domain.QScraps.scraps;
import static com.querydsl.core.group.GroupBy.groupBy;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<BoardRes> selectBoardList(String keyword, BoardCategory boardCategory, CountryTag countryTag, Long userId) {
        BooleanBuilder builder = new BooleanBuilder();

        if (keyword != null && !keyword.isEmpty()) {
            builder.and(board.title.contains(keyword))
                    .or(board.content.containsIgnoreCase(keyword));
        }
        if (boardCategory != null) {
            builder.and(board.boardCategory.eq(boardCategory));
        }
        if (countryTag != null) {
            builder.and(board.countryTag.eq(countryTag));
        }
        builder.and(board.delYn.eq("N"));

        return queryFactory.from(board)
                .where(builder)
                .leftJoin(user)
                .on(board.userId.eq(user.userId))
                .transform(groupBy(board.boardId)
                        .list(Projections.constructor(
                                BoardRes.class,
                                board,
                                user,
                                JPAExpressions.select(likes.count())
                                        .from(likes)
                                        .where(likes.boardId.eq(board.boardId)),
                                board.comments.size(),
                                JPAExpressions.select(likes.likeId)
                                        .from(likes)
                                        .where(likes.boardId.eq(board.boardId).and(likes.userId.eq(userId))),
                                JPAExpressions.select(scraps.scrapId)
                                        .from(scraps)
                                        .where(scraps.boardId.eq(scraps.boardId).and(scraps.userId.eq(userId)))
                        )));
    }
}
