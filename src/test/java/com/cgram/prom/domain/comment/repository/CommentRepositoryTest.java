package com.cgram.prom.domain.comment.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.cgram.prom.domain.comment.domain.Comment;
import com.cgram.prom.domain.feed.domain.Feed;
import com.cgram.prom.domain.feed.repository.FeedRepository;
import java.util.List;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("dev")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private FeedRepository feedRepository;

    @Test
    @DisplayName("피드로 댓글 조회하기")
    public void findByFeedIdAndIsPresent() {
        // given
        Feed feed = feedRepository.save(Feed.builder().id(UUID.randomUUID()).build());
        commentRepository.save(Comment.builder().feed(feed).build());
        commentRepository.save(Comment.builder().feed(feed).build());

        // when
        List<Comment> commentList = commentRepository.findByFeedIdAndIsPresent(feed.getId(), true);

        // then
        Assertions.assertThat(commentList.size()).isEqualTo(2);
    }

}