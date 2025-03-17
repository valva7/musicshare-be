package org.musicshare.domain.auth.service;

import io.netty.handler.codec.http.HttpHeaderValues;
import org.musicshare.domain.auth.dto.res.KakaoTokenRespDto;
import org.musicshare.domain.auth.dto.res.KakaoUserInfoRespDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class KakaoService {

    @Value("${kakao.client_id}")
    private String CLIENT_ID;

    @Value("${server-url.front}")
    private String FRONT_URL;

    @Value("${kakao.auth-url}")
    private String KAKAO_AUTH_URL;

    @Value("${kakao.user_url}")
    private String KAKAO_USER_URL;

    public String getAccessTokenFromKakao(String code) {
        String redirectUrl = FRONT_URL + "/kakao/callback";
        KakaoTokenRespDto kakaoTokenRespDto = WebClient.create(KAKAO_AUTH_URL)
            .get()
            .uri(uriBuilder -> uriBuilder
                .scheme("https")
                .path("/oauth/token")
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", CLIENT_ID)
                .queryParam("redirect_uri", redirectUrl)
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
