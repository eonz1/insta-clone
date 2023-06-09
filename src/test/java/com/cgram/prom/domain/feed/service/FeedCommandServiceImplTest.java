package com.cgram.prom.domain.feed.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.cgram.prom.domain.feed.domain.Feed;
import com.cgram.prom.domain.feed.dto.FeedLikeServiceDto;
import com.cgram.prom.domain.feed.exception.FeedException;
import com.cgram.prom.domain.feed.exception.FeedExceptionType;
import com.cgram.prom.domain.feed.repository.FeedImageRepository;
import com.cgram.prom.domain.feed.repository.FeedRepository;
import com.cgram.prom.domain.feed.repository.HashTagRepository;
import com.cgram.prom.domain.feed.request.DeleteFeedServiceDto;
import com.cgram.prom.domain.feed.request.ModifyFeedServiceDto;
import com.cgram.prom.domain.image.service.ImageService;
import com.cgram.prom.domain.profile.domain.Profile;
import com.cgram.prom.domain.profile.exception.ProfileException;
import com.cgram.prom.domain.profile.exception.ProfileExceptionType;
import com.cgram.prom.domain.profile.repository.ProfileRepository;
import com.cgram.prom.domain.statistics.service.StatisticsService;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FeedCommandServiceImplTest {

    @InjectMocks
    FeedCommandServiceImpl feedService;

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

    @Mock
    StatisticsService statisticsService;

    @Mock
    FeedLikeService feedLikeService;

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

    @Test
    @DisplayName("피드 삭제 - 프로필 존재하지 않으면 삭제 불가능")
    public void deleteFeedProfileException() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        UUID feedId = UUID.randomUUID();

        DeleteFeedServiceDto dto = DeleteFeedServiceDto.builder()
            .userId(userId)
            .feedId(feedId)
            .build();

        // when
        when(profileRepository.findByUserId(any(UUID.class))).thenReturn(Optional.empty());

        // then
        ProfileException profileException = assertThrows(ProfileException.class, () -> {
            feedService.delete(dto);
        });

        assertThat(profileException.getExceptionType()).isEqualTo(ProfileExceptionType.NOT_FOUND);

        verify(feedRepository, times(0)).findById(any(UUID.class));
    }

    @Test
    @DisplayName("피드 삭제 - 로그인 한 사용자와 피드 작성자 다르면 삭제 불가능")
    public void deleteFeedUserUnauthorized() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        UUID feedId = UUID.randomUUID();

        DeleteFeedServiceDto dto = DeleteFeedServiceDto.builder()
            .userId(userId)
            .feedId(feedId)
            .build();

        Profile profile = Profile.builder().id(UUID.randomUUID()).build();
        Feed feed = Feed.builder()
            .id(feedId)
            .profile(Profile.builder().id(UUID.randomUUID()).build())
            .build();

        // when
        when(profileRepository.findByUserId(any(UUID.class))).thenReturn(Optional.of(profile));
        when(feedRepository.findById(any(UUID.class))).thenReturn(Optional.of(feed));

        // then
        FeedException feedException = assertThrows(FeedException.class, () -> {
            feedService.delete(dto);
        });

        assertThat(feedException.getExceptionType()).isEqualTo(FeedExceptionType.UNAUTHORIZED);

        verify(feedRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    @DisplayName("피드 삭제")
    public void deleteFeed() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        UUID feedId = UUID.randomUUID();

        DeleteFeedServiceDto dto = DeleteFeedServiceDto.builder()
            .userId(userId)
            .feedId(feedId)
            .build();

        Profile profile = Profile.builder().id(UUID.randomUUID()).build();
        Feed feed = Feed.builder()
            .id(feedId)
            .profile(profile)
            .build();

        // when
        when(profileRepository.findByUserId(any(UUID.class))).thenReturn(Optional.of(profile));
        when(feedRepository.findById(any(UUID.class))).thenReturn(Optional.of(feed));

        // then
        feedService.delete(dto);

        verify(feedRepository, times(1)).findById(any(UUID.class));
        verify(statisticsService, times(1)).updateStatistics(any(UUID.class),
            anyString(), anyInt());
    }

    @Test
    @DisplayName("피드 수정")
    public void modifyFeed() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        UUID feedId = UUID.randomUUID();
        Set<String> tags = new HashSet<>();
        tags.add("#tag1");
        tags.add("#tag2");

        ModifyFeedServiceDto dto = ModifyFeedServiceDto.builder()
            .userId(userId)
            .feedId(feedId)
            .content("내용")
            .hashTags(tags)
            .build();

        Profile profile = Profile.builder().id(UUID.randomUUID()).build();
        Feed feed = Feed.builder()
            .id(feedId)
            .profile(profile)
            .content("content")
            .build();
        when(profileRepository.findByUserId(any(UUID.class))).thenReturn(Optional.of(profile));
        when(feedRepository.findById(any(UUID.class))).thenReturn(Optional.of(feed));

        // when
        feedService.modify(dto);

        // then
        verify(feedRepository, times(1)).findById(any(UUID.class));
        verify(hashTagRepository, times(1)).deleteByFeedId(any(UUID.class));
        assertThat(feed.getContent()).isEqualTo("내용");
    }

    @Test
    @DisplayName("피드 내용만 수정")
    public void modifyFeedWithoutHashtags() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        UUID feedId = UUID.randomUUID();

        ModifyFeedServiceDto dto = ModifyFeedServiceDto.builder()
            .userId(userId)
            .feedId(feedId)
            .content("내용")
            .build();

        Profile profile = Profile.builder().id(UUID.randomUUID()).build();
        Feed feed = Feed.builder()
            .id(feedId)
            .profile(profile)
            .content("content")
            .build();
        when(profileRepository.findByUserId(any(UUID.class))).thenReturn(Optional.of(profile));
        when(feedRepository.findById(any(UUID.class))).thenReturn(Optional.of(feed));

        // when
        feedService.modify(dto);

        // then
        verify(feedRepository, times(1)).findById(any(UUID.class));
        verify(hashTagRepository, times(0)).deleteByFeedId(any(UUID.class));
        assertThat(feed.getContent()).isEqualTo("내용");
    }

    @Test
    @DisplayName("피드 좋아요")
    public void like() throws Exception {
        // given
        FeedLikeServiceDto dto = FeedLikeServiceDto.builder()
            .userId(UUID.randomUUID())
            .feedID(UUID.randomUUID())
            .build();

        when(profileRepository.findByUserId(any(UUID.class))).thenReturn(
            Optional.of(Profile.builder()
                .build()));
        when(feedRepository.findById(any(UUID.class))).thenReturn(
            Optional.of(Feed.builder().build()));

        // when
        feedService.like(dto);

        // then
        verify(feedLikeService, times(1)).like(any(Feed.class), any(Profile.class));
    }

    @Test
    @DisplayName("피드 좋아요 취소")
    public void unlike() throws Exception {
        // given
        FeedLikeServiceDto dto = FeedLikeServiceDto.builder()
            .userId(UUID.randomUUID())
            .feedID(UUID.randomUUID())
            .build();

        when(profileRepository.findByUserId(any(UUID.class))).thenReturn(
            Optional.of(Profile.builder()
                .build()));
        when(feedRepository.findById(any(UUID.class))).thenReturn(
            Optional.of(Feed.builder().build()));

        // when
        feedService.unlike(dto);

        // then
        verify(feedLikeService, times(1)).unlike(any(Feed.class), any(Profile.class));
    }
}