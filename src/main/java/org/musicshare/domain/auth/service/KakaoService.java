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

    private final String CLIENT_ID;
    private final String FRONT_URL;
    private final String KAKAO_AUTH_URL;
    private final String KAKAO_USER_URL;

    public KakaoService(
        @Value("${kakao.client_id}") String clientId,
        @Value("${server-url.front}") String frontUrl,
        @Value("${kakao.auth-url}") String kakaoAuthUrl,
        @Value("${kakao.user_url}") String kakaoUserUrl
    ) {
        this.CLIENT_ID = clientId;
        this.FRONT_URL = frontUrl;
        this.KAKAO_AUTH_URL = kakaoAuthUrl;
        this.KAKAO_USER_URL = kakaoUserUrl;
    }

    /**
     * 카카오 로그인 인증 코드로 엑세스 토큰 요청
     * @param code 카카오 로그인 인증 코드
     * @return 엑세스 토큰
     */
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

    /**
     * 카카오 엑세스 토큰으로 카카오 유저 정보 요청
     * @param accessToken 카카오 엑세스 토큰
     * @return 카카오 유저 정보
     */
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
