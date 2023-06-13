package com.cgram.prom.domain.feed.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetFeedsDto {

    private int limit;

    private String cursor;

    private String tag;

    private String profileId;
}
