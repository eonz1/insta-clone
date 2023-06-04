package com.cgram.prom.domain.feed.request;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostFeedServiceDto {

    private UUID id;
    private String content;
    private List<File> images;
    private Set<String> hashtags;

    @Builder
    public PostFeedServiceDto(UUID id, String content, List<File> images,
        Set<String> hashtags) {
        this.id = id;
        this.content = content;
        this.images = images;
        this.hashtags = hashtags;
    }
}
