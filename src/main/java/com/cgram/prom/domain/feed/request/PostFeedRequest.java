package com.cgram.prom.domain.feed.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostFeedRequest {

    @Size(max = 2200, message = "글자 수를 초과했습니다.")
    private String content;

    @Valid
    @Size(max = 30, message = "해시태그 최대 개수를 초과했습니다.(30개)")
    private Set<String> hashTags;

    @Builder
    public PostFeedRequest(String content, Set<String> hashTags) {
        this.content = content;
        this.hashTags = hashTags;
    }
}
