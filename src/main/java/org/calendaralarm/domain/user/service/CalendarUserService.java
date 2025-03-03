package org.calendaralarm.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.calendaralarm.domain.user.model.dto.req.CalendarUserResDto;
import org.calendaralarm.domain.user.model.entity.CalendarUserEntity;
import org.calendaralarm.domain.user.repository.CalendarUserRepository;
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
