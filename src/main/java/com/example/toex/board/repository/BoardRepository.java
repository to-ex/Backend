package com.example.toex.board.repository;

import com.example.toex.board.domain.Board;
import com.example.toex.board.repository.impl.BoardRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {
    List<Board> findByUserId(Long userId);
}
