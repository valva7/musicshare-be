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
import org.musicshare.domain.push.repository.FcmPushRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final KakaoService kakaoService;

    private final MemberRepository memberRepository;
    private final JpaMemberRepository jpaMemberRepository;
    private final FcmPushRepository fcmPushRepository;

    private final TokenProvider tokenProvider;

    @Transactional
    public LoginTokenRes login(String code, String fcmToken) {
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

            MemberEntity newMemberEntity = memberRepository.saveMember(newMember);
            newMember.setId(newMemberEntity.getId());

            // Jwt 토큰 발급
            String refreshToken = tokenProvider.createRefreshToken(newMember);
            String accessToken = tokenProvider.createAccessToken(newMember);

            // firebase token 저장
            fcmPushRepository.firebaseTokenSave(newMember, fcmToken);

            return new LoginTokenRes(accessToken, refreshToken);
        }

        // Jwt 토큰 발급
        String refreshToken = tokenProvider.createRefreshToken(member);
        String accessToken = tokenProvider.createAccessToken(member);

        // firebase token 저장
        fcmPushRepository.firebaseTokenSave(member, fcmToken);

        return new LoginTokenRes(accessToken, refreshToken);
    }

}