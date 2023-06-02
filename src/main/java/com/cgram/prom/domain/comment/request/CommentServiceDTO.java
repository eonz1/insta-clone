package com.cgram.prom.domain.comment.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentServiceDTO {

    private String feedId;

    private String commentId;

    private String content;

    private String userId;
}
