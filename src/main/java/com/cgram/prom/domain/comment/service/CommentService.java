package com.cgram.prom.domain.comment.service;

import com.cgram.prom.domain.comment.request.CommentQueryRequest;
import com.cgram.prom.domain.comment.request.CommentServiceDTO;
import com.cgram.prom.domain.comment.response.CommentWithCountResponse;

public interface CommentService {

    CommentWithCountResponse getComments(CommentQueryRequest request);

    void reply(CommentServiceDTO dto);

    void modify(CommentServiceDTO dto);

    void delete(CommentServiceDTO dto);
}
