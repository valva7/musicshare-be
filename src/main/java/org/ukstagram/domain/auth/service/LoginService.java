package org.ukstagram.domain.auth.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ukstagram.domain.auth.utils.TokenProvider;
import org.ukstagram.domain.user.model.Member;
import org.ukstagram.domain.auth.dto.res.KakaoUserInfoRespDto;
import org.ukstagram.domain.user.model.entity.MemberEntity;
import org.ukstagram.domain.user.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final KakaoService kakaoService;

    private final MemberRepository memberRepository;

    private final TokenProvider tokenProvider;

    @Transactional
    public String login(String code) {
        String accessToken = kakaoService.getAccessTokenFromKakao(code);

        // 카카오 유저 정보 가져오기
        KakaoUserInfoRespDto userFromKakao = kakaoService.getUserFromKakao(accessToken);

        // 유저의 닉네임으로 DB에서 정보 조회 (원래는 이메일처럼 유니크한 값 사용)
        String nickName = userFromKakao.getKakaoAccount().getProfile().getNickName();
        MemberEntity memberEntity = memberRepository.findCalendarUserEntityByNickname(nickName).orElse(null);

        // 유저 정보가 없다면 유저 정보 저장
        if(memberEntity == null){
            Member member = Member.builder()
                .nickname(userFromKakao.getKakaoAccount().getProfile().getNickName())
                .build();

            MemberEntity newEntity = Member.toEntity(member);
            memberRepository.save(newEntity);

            // Jwt 토큰 발급
            return tokenProvider.createToken(newEntity.getId());
        }

        // Jwt 토큰 발급
        return tokenProvider.createToken(memberEntity.getId());
    }

}