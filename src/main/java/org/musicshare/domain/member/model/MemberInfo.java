package org.musicshare.domain.member.model;

import lombok.Getter;

@Getter
public class MemberInfo {

    private final String email;
    private final String nickname;
    private final String profileImageUrl;

    public MemberInfo(String email, String nickname, String profileImageUrl) {
        if(email == null || email.isEmpty()){
            throw new IllegalArgumentException();
        }
        if(nickname == null || nickname.isEmpty()){
            throw new IllegalArgumentException();
        }

        this.email = email;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }

}
