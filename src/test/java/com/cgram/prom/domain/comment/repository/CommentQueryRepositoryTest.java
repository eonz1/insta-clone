package com.cgram.prom.domain.comment.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.cgram.prom.domain.comment.domain.Comment;
import com.cgram.prom.domain.comment.dto.CommentDTO;
import com.cgram.prom.domain.feed.domain.Feed;
import com.cgram.prom.domain.feed.repository.FeedRepository;
import com.cgram.prom.domain.profile.domain.Profile;
import com.cgram.prom.domain.profile.repository.ProfileRepository;
import com.cgram.prom.global.config.JpaAuditingConfig;
import com.cgram.prom.global.config.QuerydslTestConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("dev")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QuerydslTestConfig.class, CommentQueryRepository.class, JpaAuditingConfig.class})
class CommentQueryRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentQueryRepository commentQueryRepository;

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Test
    @DisplayName("피드로 댓글 최상위 3개 조회하기")
    public void getCommentsByFeedIds() {
        // given
        Profile profile = Profile.builder().build();
        profileRepository.save(profile);

        Feed feed = feedRepository.save(Feed.builder().isPresent(true).build());
        Feed feed2 = feedRepository.save(Feed.builder().isPresent(true).build());
        commentRepository.save(Comment.builder().feed(feed).profile(profile).content("코멘트1").build());
        commentRepository.save(Comment.builder().feed(feed).profile(profile).content("코멘트2").build());
        commentRepository.save(Comment.builder().feed(feed).profile(profile).content("코멘트3").build());
        commentRepository.save(Comment.builder().feed(feed).profile(profile).content("코멘트4").build());
        commentRepository.save(Comment.builder().feed(feed).profile(profile).content("코멘트5").build());

        List<UUID> feedUUIDs = new ArrayList<>();
        feedUUIDs.add(feed.getId());
        feedUUIDs.add(feed2.getId());

        // when
        List<CommentDTO> commentList = commentQueryRepository.getCommentsByFeedIds(feedUUIDs, 3);

        // then
        Assertions.assertThat(commentList.size()).isEqualTo(3);
        Assertions.assertThat(commentList.get(0).getFeed().getId()).isEqualTo(feed.getId());
        Assertions.assertThat(commentList.get(1).getFeed().getId()).isEqualTo(feed.getId());
        Assertions.assertThat(commentList.get(2).getFeed().getId()).isEqualTo(feed.getId());
        Assertions.assertThat(commentList.get(0).getContent()).isEqualTo("코멘트5");
        Assertions.assertThat(commentList.get(1).getContent()).isEqualTo("코멘트4");
        Assertions.assertThat(commentList.get(2).getContent()).isEqualTo("코멘트3");
    }
}