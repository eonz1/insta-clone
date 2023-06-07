package com.cgram.prom.domain.comment.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentWithCountResponse {

    private int count;

    private List<CommentResponse> comments;

}
