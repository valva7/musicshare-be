package org.ukstagram.domain.user.dto.req;

import org.ukstagram.domain.user.model.entity.MemberEntity;

public record CalendarUserResDto(Long userId, String nickname) {

    public CalendarUserResDto(MemberEntity entity) {
        this(entity.getId(), entity.getNickname());
    }

}
