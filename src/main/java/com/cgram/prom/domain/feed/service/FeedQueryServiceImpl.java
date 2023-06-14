package com.cgram.prom.domain.feed.service;

import static java.util.stream.Collectors.groupingBy;

import com.cgram.prom.domain.comment.dto.CommentDTO;
import com.cgram.prom.domain.comment.repository.CommentQueryRepository;
import com.cgram.prom.domain.comment.response.CommentResponse;
import com.cgram.prom.domain.comment.response.CommentWithCountResponse;
import com.cgram.prom.domain.feed.domain.hashtag.HashTag;
import com.cgram.prom.domain.feed.domain.image.FeedImage;
import com.cgram.prom.domain.feed.dto.FeedDTO;
import com.cgram.prom.domain.feed.repository.FeedImageQueryRepository;
import com.cgram.prom.domain.feed.repository.FeedQueryRepository;
import com.cgram.prom.domain.feed.repository.HashTagQueryRepository;
import com.cgram.prom.domain.feed.request.GetFeedsDto;
import com.cgram.prom.domain.feed.response.FeedImageResponse;
import com.cgram.prom.domain.feed.response.FeedListResponse;
import com.cgram.prom.domain.feed.response.FeedResponse;
import com.cgram.prom.domain.statistics.enums.StatisticType;
import com.cgram.prom.domain.statistics.service.StatisticsService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FeedQueryServiceImpl implements FeedQueryService {

    private final StatisticsService statisticsService;
    private final FeedQueryRepository feedQueryRepository;
    private final HashTagQueryRepository hashTagQueryRepository;
    private final FeedImageQueryRepository feedImageQueryRepository;
    private final CommentQueryRepository commentQueryRepository;

    @Override
    public FeedListResponse getFeeds(GetFeedsDto dto) {

        // 1) feeds 가져온다
        List<FeedDTO> feeds = getFeedDtoList(dto);

        String nextId = null;

        // nextId 저장, feed 리스트에서 nextData 제거
        if (hasNext(feeds.size(), dto.getLimit())) {
            int lastFeedIndex = feeds.size() - 1;
            nextId = feeds.get(lastFeedIndex).getId().toString();
            feeds.remove(lastFeedIndex);
        }

        return getFeedListResponse(feeds, nextId);
    }

    public List<FeedDTO> getFeedDtoList(GetFeedsDto dto) {
        LocalDateTime lastDate = LocalDateTime.now().minusDays(7);

        if (dto.getTag() != null && !dto.getTag().isEmpty()) {
            // 태그로 작성자, 작성일자 상관없이 전체 조회
            return feedQueryRepository.getFeedsByHashTag(dto);
        }

        int followCount = statisticsService.getStatistics(UUID.fromString(dto.getProfileId()),
            StatisticType.FOLLOWING.label()).getCounts();

        if (followCount > 0) {
            // 내가 팔로우한 사람들의 최근 일주일 기준 피드 가져오기
            return feedQueryRepository.getFeedsByMyFollowings(dto, lastDate);
        }

        // 최근 일주일 기준 '나의' 피드 가져오기
        return feedQueryRepository.getFeedsByUser(dto, lastDate);
    }

    public boolean hasNext(int feedSize, int offset) {
        return feedSize == (offset + 1);
    }

    public FeedListResponse getFeedListResponse(List<FeedDTO> feeds, String nextId) {
        List<UUID> feedIds = feeds.stream().map(FeedDTO::getId).toList();
        List<HashTag> hashTagsByFeedIds = hashTagQueryRepository.getAllHashTagsByFeedIds(
            feedIds);
        List<FeedImage> feedImagesByFeedId = feedImageQueryRepository.getAllFeedImagesByFeedId(
            feedIds);
        List<CommentDTO> commentsByFeedIds = commentQueryRepository.getCommentsByFeedIds(feedIds,
            3);

        Map<UUID, List<HashTag>> hashTagMap = hashTagsByFeedIds.stream()
            .collect(groupingBy(x -> x.getFeed().getId()));
        Map<UUID, List<FeedImage>> feedImageMap = feedImagesByFeedId.stream()
            .collect(groupingBy(x -> x.getFeedId().getId()));
        Map<UUID, List<CommentDTO>> commentMap = commentsByFeedIds.stream()
            .collect(groupingBy(CommentDTO::getFeedId));

        List<FeedResponse> feedResponseList = new ArrayList<>();

        for (FeedDTO dto : feeds) {
            List<FeedImage> images = feedImageMap.get(dto.getId());
            FeedImageResponse coverImage = null;
            List<FeedImageResponse> feedImages = null;

            if (images != null) {

                Map<Boolean, List<FeedImage>> imageMapByIsCover = images.stream()
                    .collect(groupingBy(FeedImage::isCover));

                if (imageMapByIsCover.get(true) != null) {
                    coverImage = convertFeedImagesResponse(imageMapByIsCover.get(true)).get(0);
                }

                if (imageMapByIsCover.get(false) != null) {
                    feedImages = convertFeedImagesResponse(imageMapByIsCover.get(false));
                }
            }

            feedResponseList.add(FeedResponse.builder()
                .dto(dto)
                .coverImage(coverImage)
                .images(feedImages)
                .comments(convertCommentsResponse(commentMap.get(dto.getId())))
                .hashTags(convertHashTagResponse(hashTagMap.get(dto.getId())))
                .build());
        }

        return FeedListResponse.builder()
            .nextId(nextId)
            .feeds(feedResponseList)
            .build();
    }

    public CommentWithCountResponse convertCommentsResponse(List<CommentDTO> dtos) {
        if (dtos == null) {
            return CommentWithCountResponse.builder().count(0).build();
        }

        return CommentWithCountResponse.builder()
            .count(dtos.size())
            .comments(dtos.stream().map(CommentResponse::new).toList())
            .build();
    }

    public List<FeedImageResponse> convertFeedImagesResponse(
        List<FeedImage> feedImages) {
        if (feedImages == null) {
            return null;
        }

        return feedImages.stream().map(FeedImageResponse::new).toList();
    }

    public List<String> convertHashTagResponse(List<HashTag> hashTags) {
        if (hashTags == null) {
            return null;

        }
        return hashTags.stream().map(HashTag::getTag).toList();
    }
}
