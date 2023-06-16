package com.cgram.prom.domain.feed.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.cgram.prom.domain.feed.domain.Feed;
import com.cgram.prom.domain.feed.domain.hashtag.HashTag;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("dev")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class HashTagRepositoryTest {

    @Autowired
    private HashTagRepository hashTagRepository;
    @Autowired
    private FeedRepository feedRepository;

    @Test
    @DisplayName("feedId로 해시태그 삭제하기")
    public void deleteHashTagByFeedId() throws Exception {
        // given
        Feed feed = feedRepository.save(Feed.builder().content("내용1").build());
        List<HashTag> hashTags = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            HashTag tag = HashTag.builder()
                .tag("#tag" + i)
                .feed(feed)
                .build();
            hashTags.add(tag);
        }
        hashTagRepository.saveAll(hashTags);

        // when
        int deleteCount = hashTagRepository.deleteByFeedId(feed.getId());

        // then
        assertThat(deleteCount).isEqualTo(3);
    }
}