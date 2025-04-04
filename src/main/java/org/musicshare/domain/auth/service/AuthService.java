package org.musicshare.domain.auth.service;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final KakaoService kakaoService;
    private final StringRedisTemplate redisTemplate;
    private final TokenProvider tokenProvider;

    private final MemberRepository memberRepository;
    private final JpaMemberRepository jpaMemberRepository;
    private final FcmPushRepository fcmPushRepository;


    @Transactional
    public LoginTokenRes kakaoLogin(String code, String fcmToken) {
        String kakaoAccessToken = kakaoService.getAccessTokenFromKakao(code);

        // 카카오 유저 정보 가져오기
        KakaoUserInfoRespDto userFromKakao = kakaoService.getUserFromKakao(kakaoAccessToken);

        // 유저의 닉네임으로 DB에서 정보 조회 (원래는 이메일처럼 유니크한 값 사용)
        String nickName = userFromKakao.getKakaoAccount().getProfile().getNickName();
        Member member = memberRepository.findMemberByNickname(nickName);

        // 유저 정보가 없다면 유저 정보 저장
        if(member == null){
            // TODO: 이메일은 추후 정상적으로 입력되게 수정 예정
            MemberInfo memberInfo = new MemberInfo("", nickName, null);
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

    public boolean checkVerifyCode(@NotBlank String email, @NotBlank String code) {
        String savedCode = redisTemplate.opsForValue().get(email);
        if (savedCode == null) {
            return false;
        }
        return savedCode.equals(code);
    }
}