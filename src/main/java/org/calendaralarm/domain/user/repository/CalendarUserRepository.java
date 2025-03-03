package org.calendaralarm.domain.user.repository;

import java.util.Optional;
import org.calendaralarm.domain.user.model.entity.CalendarUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarUserRepository extends JpaRepository<CalendarUserEntity, Long> {

    Optional<CalendarUserEntity> findCalendarUserEntityByNickname(String nickname);

    CalendarUserEntity getCalendarUserEntityById(Long id);
}
