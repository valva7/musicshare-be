package org.ukstagram.domain.user.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ukstagram.domain.user.model.entity.MemberEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    private Long id;
    private String nickname;

    public static MemberEntity toEntity(Member user){
        return MemberEntity.builder()
            .nickname(user.getNickname())
            .build();
    }

}
