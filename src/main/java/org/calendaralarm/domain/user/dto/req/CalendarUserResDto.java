package org.calendaralarm.domain.user.dto.req;

import org.calendaralarm.domain.user.model.entity.CalendarUserEntity;

public record CalendarUserResDto(Long userId, String nickname) {

    public CalendarUserResDto(CalendarUserEntity entity) {
        this(entity.getId(), entity.getNickname());
    }

}
