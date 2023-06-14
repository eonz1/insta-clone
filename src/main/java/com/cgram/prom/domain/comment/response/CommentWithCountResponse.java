package com.cgram.prom.domain.comment.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentWithCountResponse {

    private int count;

    private List<CommentResponse> comments;

    @Builder
    public CommentWithCountResponse(int count, List<CommentResponse> comments) {
        this.count = count;
        this.comments = comments;
    }
}
