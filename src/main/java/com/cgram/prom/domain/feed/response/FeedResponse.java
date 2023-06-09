package com.cgram.prom.domain.feed.response;

import com.cgram.prom.domain.comment.response.CommentWithCountResponse;
import com.cgram.prom.domain.feed.dto.FeedDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
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
    private FeedImageResponse coverImage;
    private List<String> hashTags;
    private CommentWithCountResponse comments;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "Asia/Seoul")
    private LocalDateTime modifiedAt;

    private Integer likes;

    private UUID userId;
    private String userEmail;
    private UUID profileImageId;

    @Builder
    public FeedResponse(FeedDTO dto, FeedImageResponse coverImage, List<FeedImageResponse> images,
        List<String> hashTags, CommentWithCountResponse comments) {
        this.feedId = dto.getId();
        this.content = dto.getContent();
        this.createdAt = dto.getCreatedAt();
        this.modifiedAt = dto.getModifiedAt();
        this.userId = dto.getUserId();
        this.userEmail = dto.getEmail();
        this.likes = dto.getLikesCount();
        this.profileImageId = dto.getProfileImageId();

        this.images = images;
        this.coverImage = coverImage;
        this.hashTags = hashTags;
        this.comments = comments;
    }
}
