package org.musicshare.domain.auth.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.musicshare.domain.auth.dto.res.LoginTokenRes;
import org.musicshare.domain.auth.utils.TokenProvider;
import org.musicshare.domain.member.model.Member;
import org.musicshare.domain.auth.dto.res.KakaoUserInfoRespDto;
import org.musicshare.domain.member.model.MemberInfo;
import org.musicshare.domain.member.model.entity.MemberEntity;
import org.musicshare.domain.member.repository.JpaMemberRepository;
import org.musicshare.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final KakaoService kakaoService;

    private final MemberRepository memberRepository;
    private final JpaMemberRepository jpaMemberRepository;

    private final TokenProvider tokenProvider;

    @Transactional
    public LoginTokenRes login(String code) {
        String kakaoAccessToken = kakaoService.getAccessTokenFromKakao(code);

        // 카카오 유저 정보 가져오기
        KakaoUserInfoRespDto userFromKakao = kakaoService.getUserFromKakao(kakaoAccessToken);

        // 유저의 닉네임으로 DB에서 정보 조회 (원래는 이메일처럼 유니크한 값 사용)
        String nickName = userFromKakao.getKakaoAccount().getProfile().getNickName();
        Member member = memberRepository.findMemberByNickname(nickName);

        // 유저 정보가 없다면 유저 정보 저장
        if(member == null){
            MemberInfo memberInfo = new MemberInfo(nickName, null);
            Member newMember = new Member(null, memberInfo);

            MemberEntity newMemberEntity = new MemberEntity(newMember);
            jpaMemberRepository.save(newMemberEntity);

            // Jwt 토큰 발급
            String refreshToken = tokenProvider.createRefreshToken(newMemberEntity.getId());
            String accessToken = tokenProvider.createAccessToken(newMemberEntity.getId());

            return new LoginTokenRes(accessToken, refreshToken);
        }

        // Jwt 토큰 발급
        String refreshToken = tokenProvider.createRefreshToken(member.getId());
        String accessToken = tokenProvider.createAccessToken(member.getId());

        return new LoginTokenRes(accessToken, refreshToken);
    }

}