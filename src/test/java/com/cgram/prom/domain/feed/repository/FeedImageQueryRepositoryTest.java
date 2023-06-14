package com.cgram.prom.domain.feed.repository;

import com.cgram.prom.domain.feed.domain.Feed;
import com.cgram.prom.domain.feed.domain.image.FeedImage;
import com.cgram.prom.domain.image.domain.Image;
import com.cgram.prom.domain.image.repository.ImageRepository;
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
@Import({QuerydslTestConfig.class, FeedImageQueryRepository.class, JpaAuditingConfig.class})
class FeedImageQueryRepositoryTest {

    @Autowired
    private FeedImageQueryRepository feedImageQueryRepository;

    @Autowired
    FeedRepository feedRepository;

    @Autowired
    FeedImageRepository feedImageRepository;

    @Autowired
    ImageRepository imageRepository;

    @Test
    @DisplayName("피드아이디로 이미지 리스트 조회")
    public void getAllFeedImagesByFeedId() {
        // given
        Image image = saveImage();
        Image image2 = saveImage();

        Feed feed = saveFeed();
        Feed feed2 = saveFeed();

        List<UUID> feedUUIDs = new ArrayList<>();
        feedUUIDs.add(feed.getId());
        feedUUIDs.add(feed2.getId());

        saveFeedImage(image, feed);
        saveFeedImage(image2, feed);

        // when
        List<FeedImage> images = feedImageQueryRepository.getAllFeedImagesByFeedId(feedUUIDs);

        // then
        Assertions.assertThat(images.size()).isEqualTo(2);
        Assertions.assertThat(images.get(0).getImageId()).isEqualTo(image);
        Assertions.assertThat(images.get(0).getFeedId()).isEqualTo(feed);
        Assertions.assertThat(images.get(1).getImageId()).isEqualTo(image2);
        Assertions.assertThat(images.get(1).getFeedId()).isEqualTo(feed);
    }

    private Feed saveFeed() {
        Feed feed = Feed.builder()
            .profile(null)
            .isPresent(true)
            .build();
        return feedRepository.save(feed);
    }

    private Image saveImage() {
        Image image = Image.builder()
            .isPresent(true)
            .build();

        return imageRepository.save(image);
    }

    private void saveFeedImage(Image image, Feed feed) {
        FeedImage feedImage = FeedImage.builder()
            .imageId(image)
            .feedId(feed)
            .isPresent(true)
            .build();

        feedImageRepository.save(feedImage);
    }

}