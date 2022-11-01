package com.sstree.streaming.streamingserver.entity;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //무분별한 객체 생성 방지
public class Users {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id; // Primary Key

    @Column(unique = true)
    @NotNull
    private String username;

    @NotNull
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Users(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

}

