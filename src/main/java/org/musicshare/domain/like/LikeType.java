package org.musicshare.domain.like;

import lombok.Getter;

@Getter
public enum LikeType {
    MUSIC("M", "Music"),
    COMMENT("C", "Comment");

    private final String code;
    private final String description;

    LikeType(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
