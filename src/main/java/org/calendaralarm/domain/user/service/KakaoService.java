package org.calendaralarm.domain.user.service;

import io.netty.handler.codec.http.HttpHeaderValues;
import org.calendaralarm.domain.user.model.dto.res.KakaoTokenRespDto;
import org.calendaralarm.domain.user.model.dto.res.KakaoUserInfoRespDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class KakaoService {

    @Value("${kakao.client_id}")
    private String clientId;

    @Value("${kakao.redirect_uri}")
    private String redirectUri;

    private final String KAKAO_AUTH_URL = " https://kauth.kakao.com";
    private final String KAKAO_USER_URL = "https://kapi.kakao.com";


    public String getAccessTokenFromKakao(String code) {
        KakaoTokenRespDto kakaoTokenRespDto = WebClient.create(KAKAO_AUTH_URL)
            .get()
            .uri(uriBuilder -> uriBuilder
                .scheme("https")
                .path("/oauth/token")
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("code", code)
                .build())
            .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
            .retrieve()
            .bodyToMono(KakaoTokenRespDto.class)
            .block();

        return kakaoTokenRespDto.getAccessToken();
    }

    public KakaoUserInfoRespDto getUserFromKakao(String accessToken){
        return WebClient.create(KAKAO_USER_URL)
            .get()
            .uri(uriBuilder -> uriBuilder
                .scheme("https")
                .path("/v2/user/me")
                .build())
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
            .retrieve()
            .bodyToMono(KakaoUserInfoRespDto.class)
            .block();
    }

}
