package com.cgram.prom.domain.feed.dto;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FeedLikeServiceDto {

    private UUID userId;
    private UUID feedID;
}
