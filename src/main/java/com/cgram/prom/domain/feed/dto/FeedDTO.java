package com.cgram.prom.domain.feed.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FeedDTO {

    private UUID id;

    private UUID profileId;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private boolean isPresent;

    private int commentCount;

    private int likesCount;

    private String email;

    private UUID profileImageId;

    private String profileImagePath;
}
