package com.cgram.prom.domain.comment.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentWithCountResponse {

    private int totalCount;

    private List<CommentResponse> recentComments;

    @Builder
    public CommentWithCountResponse(int totalCount, List<CommentResponse> recentComments) {
        this.totalCount = totalCount;
        this.recentComments = recentComments;
    }
}
