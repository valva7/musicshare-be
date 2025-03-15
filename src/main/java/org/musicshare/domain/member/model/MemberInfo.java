package org.musicshare.domain.member.model;

import lombok.Getter;

@Getter
public class MemberInfo {

    private final String nickname;
    private final String profileImageUrl;

    public MemberInfo(String nickname, String profileImageUrl) {
        if(nickname == null || nickname.isEmpty()){
            throw new IllegalArgumentException();
        }

        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }

}
