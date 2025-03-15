package org.musicshare.domain.ai.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

// 최상위 응답 객체
@Getter
public class GptRes {

    private String id;
    private String object;
    private long created;
    private String model;
    private List<Choice> choices;
    private Usage usage;
    private String serviceTier;
    private String systemFingerprint;

    @Getter
    public static class Choice {
        private int index;
        private Message message;
        private Object logprobs;
        private String finishReason;
    }

    @Getter
    public static class Message {
        private String role;
        private String content;
        private Object refusal;
        private List<Object> annotations;
    }

    @Getter
    public static class Usage {
        @JsonProperty("prompt_tokens")
        private int promptTokens;

        @JsonProperty("completion_tokens")
        private int completionTokens;

        @JsonProperty("total_tokens")
        private int totalTokens;

        @JsonProperty("prompt_tokens_details")
        private PromptTokensDetails promptTokensDetails;

        @JsonProperty("completion_tokens_details")
        private CompletionTokensDetails completionTokensDetails;
    }

    @Getter
    public static class PromptTokensDetails {
        @JsonProperty("cached_tokens")
        private int cachedTokens;

        @JsonProperty("audio_tokens")
        private int audioTokens;
    }

    @Getter
    public static class CompletionTokensDetails {
        @JsonProperty("reasoning_tokens")
        private int reasoningTokens;

        @JsonProperty("audio_tokens")
        private int audioTokens;

        @JsonProperty("accepted_prediction_tokens")
        private int acceptedPredictionTokens;

        @JsonProperty("rejected_prediction_tokens")
        private int rejectedPredictionTokens;
    }
}
