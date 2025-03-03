package org.calendaralarm.domain.user.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.calendaralarm.global.entity.TimeBaseEntity;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "calendar_user")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@DynamicUpdate
public class CalendarUserEntity extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nickname;

    @CreatedDate
    @Column(updatable = false)
    private LocalDate regDate;

}