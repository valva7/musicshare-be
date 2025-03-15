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

    public GptRes analyzeMusicWithGPT(String audioAnalysisData) {
        // 줄바꿈을 \n 형식으로 escape 처리
        String prompt = "다음 오디오 데이터를 분석하여 음악의 분위기를 한글 키워드로 한 단어로 #을 붙여서 3개만 알려줘! \n" + audioAnalysisData;
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