package com.example.toex.board.repository;

import com.example.toex.board.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByUserIdAndBoardId(Long userId, Long boardId);
    List<Likes> findByBoardId(Long boardId);
}
