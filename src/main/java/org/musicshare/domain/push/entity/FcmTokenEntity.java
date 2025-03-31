package org.musicshare.domain.push.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="fcm_token")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FcmTokenEntity {
    @Id
    private Long userId;
    private String token;
}