package com.cgram.prom.domain.comment.controller;

import com.cgram.prom.domain.comment.dto.CommentLikeDTO;
import com.cgram.prom.domain.comment.request.CommentRequest;
import com.cgram.prom.domain.comment.request.CommentServiceDTO;
import com.cgram.prom.domain.comment.response.CommentResponse;
import com.cgram.prom.domain.comment.service.CommentLikeService;
import com.cgram.prom.domain.comment.service.CommentService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CommentController {

    private final CommentService commentService;
    private final CommentLikeService commentLikeService;

    @GetMapping("/feeds/{id}/comments")
    public ResponseEntity<List<CommentResponse>> getCommentList(@PathVariable String id) {

        return ResponseEntity.ok().body(commentService.getComments(id));
    }

    @PostMapping("/feeds/{id}/comments")
    public void reply(Authentication auth, @PathVariable String id, @RequestBody CommentRequest request) {

        CommentServiceDTO dto = CommentServiceDTO.builder()
            .feedId(id)
            .content(request.getContent())
            .userId(auth.getName())
            .build();
        commentService.reply(dto);
    }

    @PutMapping("/feeds/{feedId}/comments/{commentId}")
    public void modify(Authentication auth, @PathVariable String feedId, @PathVariable String commentId, @RequestBody CommentRequest request) {

        CommentServiceDTO dto = CommentServiceDTO.builder()
            .feedId(feedId)
            .content(request.getContent())
            .commentId(commentId)
            .userId(auth.getName())
            .build();
        commentService.modify(dto);
    }

    @DeleteMapping("/feeds/{feedId}/comments/{commentId}")
    public void delete(Authentication auth, @PathVariable String feedId, @PathVariable String commentId) {

        CommentServiceDTO dto = CommentServiceDTO.builder()
            .feedId(feedId)
            .commentId(commentId)
            .userId(auth.getName())
            .build();
        commentService.delete(dto);
    }

    @PostMapping("/feeds/{feedId}/comments/{commentId}/like")
    public void like(Authentication auth, @PathVariable String feedId, @PathVariable String commentId) {

        CommentLikeDTO dto = CommentLikeDTO.builder()
            .commentId(UUID.fromString(commentId))
            .userId(UUID.fromString(auth.getName()))
            .build();
        commentLikeService.like(dto);
    }

    @DeleteMapping("/feeds/{feedId}/comments/{commentId}/like")
    public void unlike(Authentication auth, @PathVariable String feedId, @PathVariable String commentId) {

        CommentServiceDTO dto = CommentServiceDTO.builder()
            .feedId(feedId)
            .commentId(commentId)
            .userId(auth.getName())
            .build();
        commentService.delete(dto);
    }
}
