package com.cgram.prom.domain.feed.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetFeedsDto {

    private int limit;

    private String nextId;

    private String tag;

    private String profileId;
}
