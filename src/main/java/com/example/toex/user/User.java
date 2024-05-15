package com.example.toex.user;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="user")
public class User {

    @Id
    @Column(name="user_id")
    private Long userId;

    private String name;

    private String email;
}