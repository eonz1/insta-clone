package com.cgram.prom.domain.feed.request;

import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ModifyFeedServiceDto {

    private UUID userId;
    private UUID feedId;
    private String content;
    private Set<String> hashTags;

    @Builder
    public ModifyFeedServiceDto(UUID userId, UUID feedId, String content, Set<String> hashTags) {
        this.userId = userId;
        this.feedId = feedId;
        this.content = content;
        this.hashTags = hashTags;
    }
}
