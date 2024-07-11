package com.example.toex.board.repository.impl;

import com.example.toex.board.domain.enums.BoardCategory;
import com.example.toex.board.domain.enums.CountryTag;
import com.example.toex.board.dto.res.BoardDetailRes;
import com.example.toex.board.dto.res.BoardRes;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.toex.board.domain.QBoard.board;
import static com.example.toex.user.QUser.user;
import static com.example.toex.board.domain.QLikes.likes;
import static com.example.toex.board.domain.QScraps.scraps;
import static com.example.toex.board.domain.QComment.comment;
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
                                this.getLikesCountSubquery(),
                                board.comments.size(),
                                this.getIsLikedSubquery(userId),
                                this.getIsScrappedSubquery(userId)
                        )));
    }

    @Override
    public BoardDetailRes selectBoardDetail(Long boardId, Long userId) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(board.boardId.eq(boardId));

        return queryFactory.select(Projections.constructor(
                        BoardDetailRes.class,
                        board,
                        user,
                        this.getLikesCountSubquery(),
                        board.comments.size(),
                        this.getIsLikedSubquery(userId),
                        this.getIsScrappedSubquery(userId)))
                .from(board)
                .where(builder)
                .leftJoin(user).on(board.userId.eq(user.userId))
                .fetchOne();
    }

    @Override
    public List<BoardDetailRes.CommentRes> selectCommentList(Long boardId) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(comment.board.boardId.eq(boardId));

        return queryFactory.select(Projections.constructor(
                        BoardDetailRes.CommentRes.class,
                        comment,
                        user
                ))
                .from(comment)
                .leftJoin(user).on(comment.commenterId.eq(user.userId))
                .where(builder)
                .fetch();
    }

    public Expression<Long> getLikesCountSubquery() {
        JPQLQuery<Long> where = JPAExpressions.select(likes.count())
                .from(likes)
                .where(likes.boardId.eq(board.boardId));
        return JPAExpressions.select(likes.count())
                .from(likes)
                .where(likes.boardId.eq(board.boardId));
    }

    public Expression<Long> getIsLikedSubquery(Long userId) {
        if (userId == null) {
            return Expressions.nullExpression(Long.class);
        }

        return JPAExpressions.select(likes.likeId)
                .from(likes)
                .where(likes.boardId.eq(board.boardId).and(likes.userId.eq(userId)));
    }

    public Expression<Long> getIsScrappedSubquery(Long userId) {
        if (userId == null) {
            return Expressions.nullExpression(Long.class);
        }

        return JPAExpressions.select(scraps.scrapId)
                .from(scraps)
                .where(scraps.boardId.eq(scraps.boardId).and(scraps.userId.eq(userId)));
    }
}
