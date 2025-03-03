package org.calendaralarm.domain.auth.service;

import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.calendaralarm.domain.auth.utils.TokenProvider;
import org.calendaralarm.domain.user.model.CalendarUser;
import org.calendaralarm.domain.auth.dto.res.KakaoUserInfoRespDto;
import org.calendaralarm.domain.user.model.entity.CalendarUserEntity;
import org.calendaralarm.domain.user.repository.CalendarUserRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final KakaoService kakaoService;

    private final CalendarUserRepository calendarUserRepository;

    private final TokenProvider tokenProvider;

    @Transactional
    public String login(String code) {
        String accessToken = kakaoService.getAccessTokenFromKakao(code);

        // 카카오 유저 정보 가져오기
        KakaoUserInfoRespDto userFromKakao = kakaoService.getUserFromKakao(accessToken);

        // 유저의 닉네임으로 DB에서 정보 조회 (원래는 이메일처럼 유니크한 값 사용)
        String nickName = userFromKakao.getKakaoAccount().getProfile().getNickName();
        CalendarUserEntity calendarUserEntity = calendarUserRepository.findCalendarUserEntityByNickname(nickName).get();

        // 유저 정보가 없다면 유저 정보 저장
        if(calendarUserEntity == null){
            CalendarUser calendarUser = CalendarUser.builder()
                .nickname(userFromKakao.getKakaoAccount().getProfile().getNickName())
                .build();

            CalendarUserEntity entity = CalendarUser.toEntity(calendarUser);
            calendarUserRepository.save(entity);
        }

        // Jwt 토큰 발급
        return tokenProvider.createToken(calendarUserEntity.getId());
    }

}