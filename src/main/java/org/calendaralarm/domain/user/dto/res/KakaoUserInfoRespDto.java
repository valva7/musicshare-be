package org.calendaralarm.domain.user.dto.res;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.calendaralarm.domain.user.model.KakaoAccount;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoUserInfoRespDto {
    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;
}