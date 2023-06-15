package com.cgram.prom.domain.comment.repository;

import com.cgram.prom.domain.comment.domain.like.CommentLike;
import com.cgram.prom.domain.comment.domain.like.CommentLikeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, CommentLikeId> {

}
