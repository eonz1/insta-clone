package com.cgram.prom.domain.feed.request;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeleteFeedServiceDto {

    private UUID userId;
    private UUID feedId;

    @Builder
    public DeleteFeedServiceDto(UUID userId, UUID feedId) {
        this.userId = userId;
        this.feedId = feedId;
    }
}
