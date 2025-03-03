package org.calendaralarm.domain.user.model.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.calendaralarm.domain.user.model.entity.CalendarUserEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalendarUser {

    private Long id;
    private String nickname;

    public static CalendarUserEntity toEntity(CalendarUser user){
        return CalendarUserEntity.builder()
            .nickname(user.getNickname())
            .build();
    }

}
