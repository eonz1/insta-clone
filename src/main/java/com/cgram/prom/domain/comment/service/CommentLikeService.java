package com.cgram.prom.domain.comment.service;

import com.cgram.prom.domain.comment.dto.CommentLikeDTO;

public interface CommentLikeService {

    void like(CommentLikeDTO dto);

    void unlike(CommentLikeDTO dto);

}
