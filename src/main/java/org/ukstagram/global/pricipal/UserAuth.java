package org.ukstagram.global.pricipal;

import lombok.Getter;

@Getter
public class UserAuth {
    private Long userId;
    private String nickname;

    public UserAuth(Long userId, String nickname) {
        this.userId = userId;
        this.nickname = nickname;
    }
}