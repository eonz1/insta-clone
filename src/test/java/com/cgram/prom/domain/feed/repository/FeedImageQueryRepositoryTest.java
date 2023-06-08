package com.cgram.prom.domain.feed.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.cgram.prom.domain.feed.domain.Feed;
import com.cgram.prom.domain.feed.domain.image.FeedImage;
import com.cgram.prom.domain.image.domain.Image;
import com.cgram.prom.domain.image.repository.ImageRepository;
import com.cgram.prom.global.config.JpaAuditingConfig;
import com.cgram.prom.global.config.QuerydslTestConfig;
import java.util.List;
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
    public void FeedImageQueryRepositoryTest() {
        // given
        Image image = saveImage();
        Image image2 = saveImage();

        Feed feed = Feed.builder()
            .profile(null)
            .isPresent(true)
            .build();
        feedRepository.save(feed);

        saveFeedImage(image, feed);
        saveFeedImage(image2, feed);

        // when
        List<FeedImage> images = feedImageQueryRepository.getAllFeedImagesByFeedId(feed.getId());

        // then
        Assertions.assertThat(images.size()).isEqualTo(2);
        Assertions.assertThat(images.get(0).getImageId()).isEqualTo(image);
        Assertions.assertThat(images.get(0).getFeedId()).isEqualTo(feed);
        Assertions.assertThat(images.get(1).getImageId()).isEqualTo(image2);
        Assertions.assertThat(images.get(1).getFeedId()).isEqualTo(feed);
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
            .build();

        feedImageRepository.save(feedImage);
    }

}