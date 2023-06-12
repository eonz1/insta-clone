package com.cgram.prom.domain.feed.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class FeedDTO {

    private UUID id;

    private UUID userId;
    private String email;
    private UUID profileImageId;
    private String profileImagePath;

    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    private int commentCount;
    private int likesCount;
}
