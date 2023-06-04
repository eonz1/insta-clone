package com.cgram.prom.domain.feed.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.cgram.prom.domain.feed.domain.Feed;
import com.cgram.prom.domain.feed.repository.FeedImageRepository;
import com.cgram.prom.domain.feed.repository.FeedRepository;
import com.cgram.prom.domain.feed.repository.HashTagRepository;
import com.cgram.prom.domain.image.service.ImageService;
import com.cgram.prom.domain.profile.domain.Profile;
import com.cgram.prom.domain.profile.repository.ProfileRepository;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FeedServiceImplTest {

    @InjectMocks
    FeedServiceImpl feedService;

    @Mock
    ProfileRepository profileRepository;

    @Mock
    FeedRepository feedRepository;

    @Mock
    ImageService imageService;

    @Mock
    FeedImageRepository feedImageRepository;

    @Mock
    HashTagRepository hashTagRepository;

    @Test
    @DisplayName("피드 저장")
    public void saveFeed() throws Exception {
        // given
        String content = "피드 내용";
        Profile profile = Profile.builder()
            .build();

        // when
        feedService.saveFeed(content, profile);

        // then
        verify(feedRepository, times(1)).save(any(Feed.class));
    }

    @Test
    @DisplayName("feed 이미지 저장")
    public void saveFeedImages() throws Exception {
        // given
        List<File> images = new ArrayList<>();
        File file = new File(System.getProperty("user.dir") + "/src/test/resources/test.png");
        File file2 = new File(System.getProperty("user.dir") + "/src/test/resources/mongja.png");
        images.add(file);
        images.add(file2);

        Feed feed = Feed.builder().content("content").profile(Profile.builder().build()).build();

        // when
        feedService.saveFeedImages(images, feed);

        // then
        verify(feedImageRepository, times(1)).saveAll(anyList());
    }

    @Test
    @DisplayName("해시태그 저장")
    public void saveHashTag() throws Exception {
        // given
        Feed feed = Feed.builder()
            .profile(Profile.builder().build())
            .content("피드")
            .isPresent(true)
            .build();
        Set<String> tags = new HashSet<>();
        tags.add("#tag1");
        tags.add("#tag2");

        // when
        feedService.saveHashTags(tags, feed);

        // then
        verify(hashTagRepository, times(1)).saveAll(anyList());
    }
}