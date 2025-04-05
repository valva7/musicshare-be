package org.musicshare.domain.member.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.musicshare.domain.member.model.Member;
import org.musicshare.domain.member.model.MemberInfo;
import org.musicshare.global.entity.TimeBaseEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "member", indexes = {
    @Index(name = "idx_nickname", columnList = "nickname"),
    @Index(name = "idx_email", columnList = "email"),
})
public class MemberEntity extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String password;

    private String profileImageUrl;
    private String introduceText;

    public MemberEntity(Member member) {
        this.id = member.getId();
        this.email = member.getInfo().getEmail();
        this.nickname = member.getInfo().getNickname();
        this.password = member.getInfo().getPassword();
        this.profileImageUrl = member.getInfo().getProfileImageUrl();
    }

    public Member toMember() {
        return Member.builder()
            .id(this.id)
            .info(new MemberInfo(this.email, this.nickname, this.password, this.profileImageUrl))
            .build();
    }

}