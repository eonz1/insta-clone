package com.cgram.prom.domain.comment.repository;

import com.cgram.prom.domain.comment.domain.Comment;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {

    List<Comment> findByFeedIdAndIsPresent(UUID feedId, boolean isPresent);
}
