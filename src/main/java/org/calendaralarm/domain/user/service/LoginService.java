package org.calendaralarm.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.calendaralarm.domain.user.model.CalendarUser;
import org.calendaralarm.domain.user.dto.res.KakaoUserInfoRespDto;
import org.calendaralarm.domain.user.model.entity.CalendarUserEntity;
import org.calendaralarm.domain.user.repository.CalendarUserRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final KakaoService kakaoService;

    private final CalendarUserRepository calendarUserRepository;

    public void login(String code) {
        String accessToken = kakaoService.getAccessTokenFromKakao(code);
        log.debug("accessToken : " + accessToken);

        KakaoUserInfoRespDto userFromKakao = kakaoService.getUserFromKakao(accessToken);

        if(!calendarUserRepository.existsByNickname(userFromKakao.getKakaoAccount().getProfile().getNickName())){
            CalendarUser calendarUser = CalendarUser.builder()
                .nickname(userFromKakao.getKakaoAccount().getProfile().getNickName())
                .build();

            CalendarUserEntity entity = CalendarUser.toEntity(calendarUser);
            calendarUserRepository.save(entity);
        }
    }

}