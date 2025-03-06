package org.ukstagram.domain.user.dto.req;

import org.ukstagram.domain.user.model.entity.CalendarUserEntity;

public record CalendarUserResDto(Long userId, String nickname) {

    public CalendarUserResDto(CalendarUserEntity entity) {
        this(entity.getId(), entity.getNickname());
    }

}
