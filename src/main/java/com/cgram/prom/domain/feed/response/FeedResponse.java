package com.cgram.prom.domain.feed.response;

import com.cgram.prom.domain.comment.response.CommentWithCountResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FeedResponse {

    private UUID feedId;
    private String content;
    private List<FeedImageResponse> images;
    private FeedImageResponse thumbnailImage;
    private Set<String> hashTags;
    private CommentWithCountResponse comments;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime modifiedAt;

    private Integer likes;

    @Builder
    public FeedResponse(UUID feedId, String content, List<FeedImageResponse> images,
        FeedImageResponse thumbnailImage, Set<String> hashTags, CommentWithCountResponse comments,
        LocalDateTime createdAt, LocalDateTime modifiedAt, Integer likes) {
        this.feedId = feedId;
        this.content = content;
        this.images = images;
        this.thumbnailImage = thumbnailImage;
        this.hashTags = hashTags;
        this.comments = comments;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.likes = likes;
    }
}
