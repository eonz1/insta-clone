package com.cgram.prom.domain.feed.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.cgram.prom.domain.feed.domain.Feed;
import com.cgram.prom.domain.feed.domain.hashtag.HashTag;
import com.cgram.prom.global.config.QuerydslTestConfig;
import java.util.ArrayList;
import java.util.List;
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
@Import({QuerydslTestConfig.class, FeedQueryRepository.class})
class FeedQueryRepositoryTest {

    @Autowired
    private FeedQueryRepository feedQueryRepository;

    @Autowired
    FeedRepository feedRepository;

    @Autowired
    HashTagRepository hashTagRepository;

    @Test
    @DisplayName("해시태그로 피드 조회하기")
    public void getFeedsByHashtag() throws Exception {
        // given
        List<Feed> feeds = new ArrayList<>();
        List<HashTag> hashTags = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Feed feed = Feed.builder()
                .content("피드" + (i + 1))
                .isPresent(true)
                .build();

            HashTag tag = HashTag.builder()
                .tag("#tag")
                .feed(feed)
                .isPresent(true)
                .build();
            feed.addHashTag(tag);
            hashTags.add(tag);
            if (i % 2 == 0) {
                HashTag tag2 = HashTag.builder().tag("#태그").feed(feed).isPresent(true).build();
                feed.addHashTag(tag2);
                hashTags.add(tag2);
            }
            feeds.add(feed);
        }
        hashTagRepository.saveAll(hashTags);
        feedRepository.saveAll(feeds);

        // when
        List<Feed> allFeedsByHashTag = feedQueryRepository.getAllFeedsByHashTag("#태그");
        List<Feed> allFeedsByHashTag2 = feedQueryRepository.getAllFeedsByHashTag("#tag");

        // then
        assertThat(allFeedsByHashTag.size()).isEqualTo(5);
        assertThat(allFeedsByHashTag2.size()).isEqualTo(10);

    }
}