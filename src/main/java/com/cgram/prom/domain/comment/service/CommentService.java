package com.cgram.prom.domain.comment.service;

import com.cgram.prom.domain.comment.request.CommentServiceDTO;
import com.cgram.prom.domain.comment.response.CommentResponse;
import java.util.List;

public interface CommentService {

    List<CommentResponse> getComments(String feedId);

    void reply(CommentServiceDTO dto);

    void modify(CommentServiceDTO dto);

    void delete(CommentServiceDTO dto);
}
