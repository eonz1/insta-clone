package com.cgram.prom.domain.feed.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.cgram.prom.domain.feed.domain.Feed;
import com.cgram.prom.domain.feed.domain.hashtag.HashTag;
import com.cgram.prom.domain.profile.domain.Profile;
import com.cgram.prom.global.config.JpaAuditingConfig;
import com.cgram.prom.global.config.QuerydslTestConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
@Import({QuerydslTestConfig.class, HashTagQueryRepository.class, JpaAuditingConfig.class})
class HashTagQueryRepositoryTest {

    @Autowired
    HashTagQueryRepository hashTagQueryRepository;

    @Autowired
    FeedRepository feedRepository;

    @Autowired
    HashTagRepository hashTagRepository;

    private Feed saveFeed(String content, Profile profile) {
        Feed feed = Feed.builder()
            .content(content)
            .profile(profile)
            .isPresent(true)
            .build();

        return feedRepository.save(feed);
    }

    private HashTag saveHashTag(String tag, Feed feed) {
        HashTag hashTag = HashTag.builder()
            .tag(tag)
            .feed(feed)
            .build();

        return hashTagRepository.save(hashTag);
    }

    @Test
    @DisplayName("피드 아이디 리스트로 해시태그 가져오기")
    public void getAllHashTagsByFeedIds() throws Exception {
        // given
        List<Feed> feeds = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Feed feed = saveFeed("피드" + (i + 1), null);
            feeds.add(feed);

            saveHashTag("태그1", feed);
            saveHashTag("태그2", feed);

            if (i % 2 == 0) {
                saveHashTag("태그3", feed);
            }
        }

        List<UUID> feedIds = feeds.stream().map(Feed::getId).toList();

        // when
        List<HashTag> allHashTagsByFeedIds = hashTagQueryRepository.getAllHashTagsByFeedIds(
            feedIds);

        // then
        assertThat(allHashTagsByFeedIds.size()).isEqualTo(13);
    }
}