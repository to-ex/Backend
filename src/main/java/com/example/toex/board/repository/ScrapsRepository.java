package com.example.toex.board.repository;

import com.example.toex.board.domain.Scraps;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScrapsRepository extends JpaRepository<Scraps, Long> {
    Optional<Scraps> findByUserIdAndBoardId(Long userId, Long boardId);
}
