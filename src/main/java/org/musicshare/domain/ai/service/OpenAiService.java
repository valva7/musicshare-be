package org.musicshare.domain.ai.service;

import io.netty.handler.codec.http.HttpHeaderValues;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import org.musicshare.domain.ai.dto.res.GptRes;

@Service
public class OpenAiService {

    @Value("${gpt.url}")
    private String gptApiUri;

    @Value("${gpt.api-key}")
    private String gptApiKey;

    private final String MOODS = "[밝은, 신나는, 경쾌한, 행복한, 청량한, 귀여운, 코믹한, 희망한, 로맨틱, 사랑스러운, 슬픈, 어두운, 우울한, 쓸쓸한, 아련한, 잔잔한, 웅장한, 비장한, 긴장감, 화난, 박력, 공포, 몽환적인, 신비로운, 나른한, 미래적인, 그루브, 섹시한, 단순한, 중독성, 난해한, 이국적인, 한국적인]";

    /**
     * 음악 분석을 위한 GPT API 호출
     *
     * @param audioAnalysisData 오디오 분석 데이터
     * @return GptRes GPT 응답 객체
     */
    public GptRes analyzeMusicWithGPT(String audioAnalysisData) {
        // 줄바꿈을 \n 형식으로 escape 처리
        String prompt = "다음 오디오 데이터를 분석하여 음악의 분위기를 한글 키워드로 한 단어로 #을 붙여서 " + MOODS + " 중에 골라서 " + " 3개만 알려줘! \n" + audioAnalysisData;
        String cleanedPrompt = prompt.replace("\n", "\\n");

        // JSON 요청 본문
        String requestBody = "{"
            + "\"model\": \"gpt-3.5-turbo\", "
            + "\"messages\": [{ "
            + "\"role\": \"user\", "
            + "\"content\": \"" + cleanedPrompt + "\" "
            + "}]"
            + "}";

        return WebClient.create(gptApiUri)
            .post()
            .uri(UriBuilder::build)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + gptApiKey)
            .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON.toString())
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(GptRes.class)
            .block();
    }
}