package com.cgram.prom.domain.comment.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentWithCountResponse {

    private int totalCount;

    private List<CommentResponse> comments;

    private String nextId;

    @Builder
    public CommentWithCountResponse(int totalCount, List<CommentResponse> comments, String nextId) {
        this.totalCount = totalCount;
        this.comments = comments;
        this.nextId = nextId;
    }
}
