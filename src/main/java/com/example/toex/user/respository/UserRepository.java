package com.example.toex.user.respository;

import com.example.toex.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    User findByUserId(Long userId);

    Optional<User> findByName(String name);

    // 소프트 삭제되지 않은 사용자만 조회
    Optional<User> findByEmailAndDelYn(String email, String delYn);
}