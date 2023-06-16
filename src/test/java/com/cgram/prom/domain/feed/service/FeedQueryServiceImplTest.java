package com.cgram.prom.domain.feed.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.cgram.prom.domain.comment.dto.CommentDTO;
import com.cgram.prom.domain.comment.repository.CommentQueryRepository;
import com.cgram.prom.domain.comment.response.CommentWithCountResponse;
import com.cgram.prom.domain.feed.domain.Feed;
import com.cgram.prom.domain.feed.domain.hashtag.HashTag;
import com.cgram.prom.domain.feed.domain.image.FeedImage;
import com.cgram.prom.domain.feed.dto.FeedDTO;
import com.cgram.prom.domain.feed.repository.FeedImageQueryRepository;
import com.cgram.prom.domain.feed.repository.FeedQueryRepository;
import com.cgram.prom.domain.feed.repository.HashTagQueryRepository;
import com.cgram.prom.domain.feed.request.GetFeedsDto;
import com.cgram.prom.domain.feed.response.FeedImageResponse;
import com.cgram.prom.domain.feed.response.FeedListResponse;
import com.cgram.prom.domain.image.domain.Image;
import com.cgram.prom.domain.statistics.domain.Statistics;
import com.cgram.prom.domain.statistics.service.StatisticsService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FeedQueryServiceImplTest {

    @InjectMocks
    FeedQueryServiceImpl feedQueryService;
    @Mock
    StatisticsService statisticsService;
    @Mock
    FeedQueryRepository feedQueryRepository;
    @Mock
    HashTagQueryRepository hashTagQueryRepository;
    @Mock
    FeedImageQueryRepository feedImageQueryRepository;
    @Mock
    CommentQueryRepository commentQueryRepository;


    @Test
    @DisplayName("feed 조회 시 nextData 있으면 조회 리스트에서 마지막 데이터 삭제, nextId만 저장")
    void getFeedsWithNextId() {
        List<FeedDTO> dtos = new ArrayList<>();
        UUID nextId = null;
        int limit = 4;
        for (int i = 0; i < limit + 1; i++) {
            UUID id = UUID.randomUUID();
            dtos.add(FeedDTO.builder()
                .id(id)
                .build());

            if (i == limit) {
                nextId = id;
            }
        }
        GetFeedsDto dto = GetFeedsDto.builder().tag("tag").limit(limit).build();
        when(feedQueryService.getFeedDtoList(dto)).thenReturn(dtos);

        FeedListResponse feedListResponse = feedQueryService.getFeeds(dto);

        assertThat(feedListResponse.getNextId()).isEqualTo(nextId.toString());
        assertThat(feedListResponse.getFeeds().size()).isEqualTo(limit);
    }

    @Test
    @DisplayName("feed 조회 시 limit보다 데이터 수가 적으면 nextId null")
    void getFeeds() {
        List<FeedDTO> dtos = new ArrayList<>();
        UUID nextId = null;
        int limit = 4;
        for (int i = 0; i < limit - 2; i++) {
            UUID id = UUID.randomUUID();
            dtos.add(FeedDTO.builder()
                .id(id)
                .build());

            if (i == (limit - 2 - 1)) {
                nextId = id;
            }
        }
        GetFeedsDto dto = GetFeedsDto.builder().tag("tag").limit(limit).build();
        when(feedQueryService.getFeedDtoList(dto)).thenReturn(dtos);

        FeedListResponse feedListResponse = feedQueryService.getFeeds(dto);

        assertThat(feedListResponse.getNextId()).isNull();
        assertThat(feedListResponse.getFeeds().size()).isLessThan(limit);
    }

    @Test
    @DisplayName("dto에 tag가 포함되어 있으면 tag")
    void getFeedDtoTagList() {
        // given
        GetFeedsDto dto = GetFeedsDto.builder()
            .tag("tag")
            .limit(5)
            .build();

        // when
        feedQueryService.getFeedDtoList(dto);

        // then
        verify(feedQueryRepository, times(1)).getFeedsByHashTag(dto);
        verify(statisticsService, times(0)).getStatistics(any(UUID.class), anyString());
        verify(feedQueryRepository, times(0)).getFeedsByMyFollowings(any(GetFeedsDto.class), any(
            LocalDateTime.class));
        verify(feedQueryRepository, times(0)).getFeedsByUser(any(GetFeedsDto.class), any(
            LocalDateTime.class));
    }

    @Test
    @DisplayName("홈피드 - 내가 팔로우 한 사람들 조회")
    void getFeedDtoFollowingList() {
        // given
        GetFeedsDto dto = GetFeedsDto.builder()
            .profileId(UUID.randomUUID().toString())
            .limit(5)
            .build();
        Statistics statistics = Statistics.builder().counts(2).build();
        when(statisticsService.getStatistics(any(UUID.class), anyString())).thenReturn(statistics);

        // when
        feedQueryService.getFeedDtoList(dto);

        // then
        verify(feedQueryRepository, times(0)).getFeedsByHashTag(dto);
        verify(statisticsService, times(1)).getStatistics(any(UUID.class), anyString());
        verify(feedQueryRepository, times(1)).getFeedsByMyFollowings(any(GetFeedsDto.class), any(
            LocalDateTime.class));
        verify(feedQueryRepository, times(0)).getFeedsByUser(any(GetFeedsDto.class), any(
            LocalDateTime.class));
    }

    @Test
    @DisplayName("홈피드 - 내가 팔로우 한 사람들 없으면 내 피드 조회")
    void getFeedDtoMyList() {
        // given
        GetFeedsDto dto = GetFeedsDto.builder()
            .profileId(UUID.randomUUID().toString())
            .limit(5)
            .build();
        Statistics statistics = Statistics.builder().counts(0).build();
        when(statisticsService.getStatistics(any(UUID.class), anyString())).thenReturn(statistics);

        // when
        feedQueryService.getFeedDtoList(dto);

        // then
        verify(feedQueryRepository, times(0)).getFeedsByHashTag(dto);
        verify(statisticsService, times(1)).getStatistics(any(UUID.class), anyString());
        verify(feedQueryRepository, times(0)).getFeedsByMyFollowings(any(GetFeedsDto.class), any(
            LocalDateTime.class));
        verify(feedQueryRepository, times(1)).getFeedsByUser(any(GetFeedsDto.class), any(
            LocalDateTime.class));
    }

    @Test
    @DisplayName("피드 리스트 FeedListResponse 타입으로 변환")
    void getFeedListResponse() {
        // given
        List<FeedDTO> feeds = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            feeds.add(FeedDTO.builder()
                .id(UUID.randomUUID())
                .content("feed" + (i + 1))
                .commentCount(3)
                .likesCount(5)
                .build());
        }

        List<HashTag> hashTags = new ArrayList<>();
        List<FeedImage> images = new ArrayList<>();
        List<CommentDTO> comments = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Feed feed = Feed.builder().id(feeds.get(i).getId()).content("feed" + (i + 1)).build();
            Image image = Image.builder()
                .id(UUID.randomUUID())
                .path("feedPath" + (i + 1))
                .isPresent(true)
                .build();
            images.add(FeedImage.builder()
                .feedId(feed)
                .imageId(image)
                .isCover(false)
                .build());
            if (i == 0) {
                images.add(FeedImage.builder()
                    .feedId(feed)
                    .imageId(Image.builder()
                        .id(UUID.randomUUID())
                        .path("coverPath" + (i + 1))
                        .isPresent(true)
                        .build())
                    .isCover(true)
                    .build());
            }
            hashTags.add(HashTag.builder().tag("tag" + (i + 1))
                .feed(feed).build());
            comments.add(CommentDTO.builder()
                .feedId(feed.getId())
                .content("comment" + (i + 1))
                .build());
        }
        List<UUID> ids = feeds.stream().map(FeedDTO::getId).toList();
        when(hashTagQueryRepository.getAllHashTagsByFeedIds(ids)).thenReturn(hashTags);
        when(feedImageQueryRepository.getAllFeedImagesByFeedId(ids)).thenReturn(images);
        when(commentQueryRepository.getCommentsByFeedIds(ids, 3)).thenReturn(comments);

        // when
        FeedListResponse feedListResponse = feedQueryService.getFeedListResponse(feeds, null);

        // then
        assertThat(feedListResponse.getFeeds().size()).isEqualTo(3);
        assertThat(feedListResponse.getFeeds().get(0).getHashTags().size()).isEqualTo(1);
        assertThat(feedListResponse.getFeeds().get(0).getImages().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("댓글 리스트 CommentWithCountResponse 타입으로 변환")
    void convertCommentsResponse() {
        // given
        List<CommentDTO> dtos = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            dtos.add(new CommentDTO());
        }

        // when
        CommentWithCountResponse commentWithCountResponse = feedQueryService.convertCommentsResponse(
            dtos);

        // then
        assertThat(commentWithCountResponse.getRecentComments().size()).isEqualTo(3);
        assertThat(commentWithCountResponse.getTotalCount()).isEqualTo(3);
    }

    @Test
    @DisplayName("댓글 리스트 null이면 CommentWithCountResponse 기본 타입으로 반환")
    void convertCommentsResponseNull() {
        // given
        List<CommentDTO> dtos = null;

        // when
        CommentWithCountResponse commentWithCountResponse = feedQueryService.convertCommentsResponse(
            dtos);

        // then
        assertThat(commentWithCountResponse.getRecentComments()).isNull();
        assertThat(commentWithCountResponse.getTotalCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("피드 이미지 리스트 Response 타입으로 변환")
    void convertFeedImagesResponse() {
        // given
        List<FeedImage> feedImages = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            feedImages.add(FeedImage.builder()
                .imageId(
                    Image.builder().path("path" + (i + 1)).id(UUID.randomUUID()).isPresent(true)
                        .build())
                .feedId(Feed.builder().build())
                .isPresent(true)
                .build());
        }

        // when
        List<FeedImageResponse> feedImageResponses = feedQueryService.convertFeedImagesResponse(
            feedImages);

        // then
        assertThat(feedImageResponses.get(0).getImageId()).isEqualTo(
            feedImages.get(0).getImageId().getId());

        assertThat(feedImageResponses.get(1).getImageId()).isEqualTo(
            feedImages.get(1).getImageId().getId());
    }

    @Test
    @DisplayName("피드 이미지 리스트가 null이면 빈 배열 반환")
    void convertFeedImagesResponseNull() {
        // given
        List<FeedImage> feedImages = null;

        // when
        List<FeedImageResponse> feedImageResponses = feedQueryService.convertFeedImagesResponse(
            feedImages);

        // then
        assertThat(feedImageResponses.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("해시태그 리스트가 null이면 null 반환")
    void convertHashTagResponseNull() {
        // given
        List<HashTag> hashTags = null;

        // when
        List<String> hashTagResponse = feedQueryService.convertHashTagResponse(hashTags);

        // then
        assertThat(hashTagResponse).isNull();
    }

    @Test
    @DisplayName("해시태그 엔티티 리스트에서 tag만 뽑아서 List<String>으로 변경")
    void convertHashTagResponse() {
        // given
        List<HashTag> hashTags = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            hashTags.add(HashTag.builder()
                .tag("tag" + (i + 1))
                .build());
        }

        // when
        List<String> hashTagResponse = feedQueryService.convertHashTagResponse(hashTags);

        // then
        assertThat(hashTagResponse.get(0)).isEqualTo("tag1");
        assertThat(hashTagResponse.get(1)).isEqualTo("tag2");
        assertThat(hashTagResponse.get(2)).isEqualTo("tag3");
    }

}