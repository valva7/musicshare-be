package org.calendaralarm.global.pricipal;

import lombok.Getter;

@Getter
public class UserPrincipal {
    private Long userId;

    public UserPrincipal(Long userId) {
        this.userId = userId;
    }
}