package org.musicshare.domain.auth.service;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.musicshare.domain.auth.dto.res.LoginTokenRes;
import org.musicshare.domain.member.model.Member;
import org.musicshare.domain.auth.dto.res.KakaoUserInfoRespDto;
import org.musicshare.domain.member.model.MemberInfo;
import org.musicshare.domain.member.model.entity.MemberEntity;
import org.musicshare.domain.member.repository.JpaMemberRepository;
import org.musicshare.domain.member.repository.MemberRepository;
import org.musicshare.domain.auth.dto.req.KakaoLoginReq;
import org.musicshare.domain.auth.dto.req.LoginReq;
import org.musicshare.domain.auth.dto.req.MemberSignupReq;
import org.musicshare.domain.push.repository.FcmPushRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;


    /**
     * 카카오 로그인
     * @param req
     * @return
     */
    @Transactional
    public LoginTokenRes kakaoLogin(KakaoLoginReq req) {
        String kakaoAccessToken = kakaoService.getAccessTokenFromKakao(req.code());

        // 카카오 유저 정보 가져오기
        KakaoUserInfoRespDto userFromKakao = kakaoService.getUserFromKakao(kakaoAccessToken);

        // TODO: 기존 회원가입 유저인지 확인 예정
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
            fcmPushRepository.firebaseTokenSave(newMember, req.fcmToken());

            return new LoginTokenRes(accessToken, refreshToken);
        }

        // Jwt 토큰 발급
        String refreshToken = tokenProvider.createRefreshToken(member);
        String accessToken = tokenProvider.createAccessToken(member);

        // firebase token 저장
        fcmPushRepository.firebaseTokenSave(member, req.fcmToken());

        return new LoginTokenRes(accessToken, refreshToken);
    }

    /**
     * 로그인
     * @param req
     * @return
     */
    public LoginTokenRes login(LoginReq req) {
        // 이메일로 회원 정보 조회
        Member member = memberRepository.findMemberByEmail(req.email());
        if (member == null) {
            throw new IllegalArgumentException("로그인 정보가 틀렸습니다.");
        }
        // 비밀번호 확인
        if (!passwordEncoder.matches(req.password(), member.getInfo().getPassword())) {
            throw new IllegalArgumentException("로그인 정보가 틀렸습니다.");
        }

        // firebase token 저장
        fcmPushRepository.firebaseTokenSave(member, req.fcmToken());

        // Jwt 토큰 발급
        String refreshToken = tokenProvider.createRefreshToken(member);
        String accessToken = tokenProvider.createAccessToken(member);

        return new LoginTokenRes(accessToken, refreshToken);
    }

    /**
     * 이메일 인증 코드 확인
     * @param email
     * @param code
     * @return
     */
    public boolean checkVerifyCode(@NotBlank String email, @NotBlank String code) {
        String savedCode = redisTemplate.opsForValue().get(email);
        return savedCode != null && savedCode.equals(code);
    }

    /**
     * 닉네임 중복 확인
     * @param nickname
     * @return
     */
    public Boolean validateNickname(String nickname) {
        return jpaMemberRepository.existsByNickname(nickname);
    }

    /**
     * 회원가입
     * @param req
     */
    public void signUp(MemberSignupReq req) {
        if (jpaMemberRepository.existsByEmail(req.emailFull())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        if (jpaMemberRepository.existsByNickname(req.nickname())) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }
        if (!req.password().equals(req.passwordConfirm())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String encodedPassword = passwordEncoder.encode(req.password());
        MemberInfo memberInfo = new MemberInfo(req.emailFull(), req.nickname(), encodedPassword,null);
        Member newMember = new Member(null, memberInfo);

        memberRepository.saveMember(newMember);
    }

}