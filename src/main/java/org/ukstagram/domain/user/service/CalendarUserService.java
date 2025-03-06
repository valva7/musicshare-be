package org.ukstagram.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.ukstagram.domain.user.dto.req.CalendarUserResDto;
import org.ukstagram.domain.user.model.entity.CalendarUserEntity;
import org.ukstagram.domain.user.repository.CalendarUserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CalendarUserService {

    private final CalendarUserRepository calendarUserRepository;

    public CalendarUserResDto getUser(Long userId) {
        CalendarUserEntity entity = calendarUserRepository.getCalendarUserEntityById(userId);
        return new CalendarUserResDto(entity);
    }

}
