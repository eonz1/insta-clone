package com.cgram.prom.domain.feed.service;

import com.cgram.prom.domain.comment.domain.Comment;
import com.cgram.prom.domain.comment.repository.CommentQueryRepository;
import com.cgram.prom.domain.comment.response.CommentResponse;
import com.cgram.prom.domain.comment.response.CommentWithCountResponse;
import com.cgram.prom.domain.feed.domain.Feed;
import com.cgram.prom.domain.feed.domain.hashtag.HashTag;
import com.cgram.prom.domain.feed.domain.image.FeedImage;
import com.cgram.prom.domain.feed.dto.FeedDTO;
import com.cgram.prom.domain.feed.exception.FeedException;
import com.cgram.prom.domain.feed.exception.FeedExceptionType;
import com.cgram.prom.domain.feed.repository.FeedImageQueryRepository;
import com.cgram.prom.domain.feed.repository.FeedImageRepository;
import com.cgram.prom.domain.feed.repository.FeedQueryRepository;
import com.cgram.prom.domain.feed.repository.FeedRepository;
import com.cgram.prom.domain.feed.repository.HashTagQueryRepository;
import com.cgram.prom.domain.feed.repository.HashTagRepository;
import com.cgram.prom.domain.feed.request.DeleteFeedServiceDto;
import com.cgram.prom.domain.feed.request.GetFeedsServiceDto;
import com.cgram.prom.domain.feed.request.ModifyFeedServiceDto;
import com.cgram.prom.domain.feed.request.PostFeedServiceDto;
import com.cgram.prom.domain.feed.response.FeedImageResponse;
import com.cgram.prom.domain.feed.response.FeedListResponse;
import com.cgram.prom.domain.feed.response.FeedListResponse.FeedListResponseBuilder;
import com.cgram.prom.domain.feed.response.FeedResponse;
import com.cgram.prom.domain.image.domain.Image;
import com.cgram.prom.domain.image.service.ImageService;
import com.cgram.prom.domain.profile.domain.Profile;
import com.cgram.prom.domain.profile.exception.ProfileException;
import com.cgram.prom.domain.profile.exception.ProfileExceptionType;
import com.cgram.prom.domain.profile.repository.ProfileRepository;
import com.cgram.prom.domain.statistics.enums.StatisticType;
import com.cgram.prom.domain.statistics.service.StatisticsService;
import jakarta.transaction.Transactional;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {

    private final ProfileRepository profileRepository;
    private final FeedRepository feedRepository;
    private final ImageService imageService;
    private final FeedImageRepository feedImageRepository;
    private final HashTagRepository hashTagRepository;
    private final StatisticsService statisticsService;
    private final FeedQueryRepository feedQueryRepository;
    private final HashTagQueryRepository hashTagQueryRepository;
    private final FeedImageQueryRepository feedImageQueryRepository;
    private final CommentQueryRepository commentQueryRepository;

    @Override
    public FeedListResponse getFeeds(GetFeedsServiceDto dto) {

        // 1) feeds 가져온다
        List<FeedDTO> feeds = getFeedEntityList(dto);
        FeedListResponseBuilder builder = FeedListResponse.builder();

        // nextId 확인하고 있으면 빼고, feed 리스트에서 제거하기
        if (hasNext(feeds.size(), dto.getLimit())) {
            int lastFeedIndex = feeds.size() - 1;
            builder.nextId(feeds.get(lastFeedIndex).getId().toString());
            feeds.remove(lastFeedIndex);
        }

        // 2) 연관된 테이블 가져온다
        List<UUID> feedIds = feeds.stream().map(FeedDTO::getId).toList();
        List<HashTag> allHashTagsByFeedIds = hashTagQueryRepository.getAllHashTagsByFeedIds(
                feedIds);
        List<FeedImage> allFeedImagesByFeedId = feedImageQueryRepository.getAllFeedImagesByFeedId(feedIds);
        List<Comment> allCommentsByFeedIds = commentQueryRepository.getAllCommentsByFeedIds(feedIds, 3);


        Map<UUID, List<HashTag>> hashTagMap = allHashTagsByFeedIds.stream()
                .collect(Collectors.groupingBy(x -> x.getFeed().getId()));
        Map<UUID, List<FeedImage>> feedImageMap = allFeedImagesByFeedId.stream().collect(Collectors.groupingBy(x -> x.getFeedId().getId()));
        Map<UUID, List<Comment>> commentMap = allCommentsByFeedIds.stream().collect(Collectors.groupingBy(x -> x.getFeed().getId()));

        // 3) dto로 만들어서 반환한다
        List<FeedResponse> feedResponseList = new ArrayList<>();
        for (FeedDTO feedDTO : feeds) {
            Feed feed = Feed.builder()
                    .id(feedDTO.getId())
                    .content(feedDTO.getContent())
                    .isPresent(feedDTO.isPresent())
                    .build();

            feed.setHashTags(hashTagMap.get(feedDTO.getId()));
            feed.setImages(feedImageMap.get(feedDTO.getId()));

            feedResponseList.add(convertFeedResponseWithFeedAndDto(feed, feedDTO));
        }
        return builder
                .feeds(feedResponseList)
                .build();
    }

    private boolean hasNext(int feedSize, int offset) {
        return feedSize == (offset + 1);
    }

    private List<FeedDTO> getFeedEntityList(GetFeedsServiceDto dto) {
        LocalDateTime lastDate = LocalDateTime.now().minusDays(7);

        if (dto.getTag() != null && !dto.getTag().isEmpty()) {
            // 태그로 작성자, 작성일자 상관없이 전체 조회
            return feedQueryRepository.getAllFeedsByHashTag(dto, lastDate);
        }

        int followCount = statisticsService.getStatistics(UUID.fromString(dto.getProfileId()),
                StatisticType.FOLLOWING.label()).getCounts();

        if (followCount > 0) {
            // 내가 팔로우한 사람들의 최근 일주일 기준 피드 가져오기
            return feedQueryRepository.getAllFeedsByMyFollowings(dto, lastDate);
        }

        // 최근 일주일 기준 '나의' 피드 가져오기
        return feedQueryRepository.getAllFeedsByMyProfile(dto, lastDate);
    }

    private FeedResponse convertFeedResponseWithFeedAndDto(Feed feed, FeedDTO feedDto) {
        Set<String> hashTags = getHashTagResponseType(feed.getHashTags());

        List<FeedImageResponse> feedImageResponses = new ArrayList<>();
        FeedImageResponse thumbnailImage = new FeedImageResponse();

        if (feed.getImages() != null) {
            Map<Boolean, List<FeedImage>> collect = feed.getImages().stream().collect(Collectors.groupingBy(FeedImage::isCover));

            if (collect.get(true) != null) {
                thumbnailImage = getFeedImageResponse(collect.get(true).get(0));
            }

            for (FeedImage image : collect.get(false)) {
                feedImageResponses.add(getFeedImageResponse(image));
            }
        }
        return FeedResponse.builder()
                .feedId(feed.getId())
                .content(feed.getContent())
                .images(feedImageResponses)
                .thumbnailImage(thumbnailImage)
                .hashTags(hashTags)
                .comments(CommentWithCountResponse.builder()
                        .count(feedDto.getCommentCount())
                        .build())
                .createdAt(feedDto.getCreatedAt())
                .modifiedAt(feedDto.getModifiedAt())
                .likes(feedDto.getLikesCount())
                .profileId(feedDto.getProfileId())
                .profileImageId(feedDto.getProfileImageId())
                .profileImagePath(feedDto.getProfileImagePath())
                .build();
    }

    @Override
    public FeedResponse getFeed(UUID feedId) {
        Feed feed = feedRepository.findByIdAndIsPresent(feedId, true)
                .orElseThrow(() -> new FeedException(FeedExceptionType.NOT_FOUND));

        return getFeedResponse(feed);
    }

    private FeedResponse getFeedResponse(Feed feed) {
        Set<String> hashTags = getHashTagResponseType(feed.getHashTags());

        List<FeedImageResponse> feedImageResponses = new ArrayList<>();
        FeedImageResponse thumbnailImage = new FeedImageResponse();

        for (FeedImage image : feed.getImages()) {
            if (image.isCover()) {
                thumbnailImage = getFeedImageResponse(image);
                continue;
            }
            feedImageResponses.add(getFeedImageResponse(image));
        }

        return FeedResponse.builder()
                .content(feed.getContent())
                .modifiedAt(feed.getModifiedAt())
                .createdAt(feed.getCreatedAt())
                .feedId(feed.getId())
                .hashTags(hashTags)
                .images(feedImageResponses)
                .thumbnailImage(thumbnailImage)
                .likes(0)
                .build();
    }

    private Set<String> getHashTagResponseType(List<HashTag> hashTags) {
        System.out.println("태그 확인 " + hashTags);
        if (hashTags == null) {
            return new HashSet<>();

        }
        return hashTags.stream().map(HashTag::getTag).collect(Collectors.toSet());
    }

    private FeedImageResponse getFeedImageResponse(FeedImage feedImage) {
        return FeedImageResponse.builder()
                .imageId(feedImage.getImageId().getId())
                .path(feedImage.getImageId().getPath())
                .imageIndex(feedImage.getImageIndex())
                .build();
    }

    @Transactional
    @Override
    public void post(PostFeedServiceDto dto) {
        Profile profile = profileRepository.findByUserId(dto.getId())
                .orElseThrow(() -> new ProfileException(
                        ProfileExceptionType.NOT_FOUND));

        // 피드 저장
        Feed feed = saveFeed(dto.getContent(), profile);

        // 피드 이미지 저장
        saveFeedImages(dto.getImages(), feed);

        // 해시태그 저장
        saveHashTags(dto.getHashtags(), feed);

        // 프로필에 피드 수 추가
        statisticsService.updateStatistics(profile.getId(), StatisticType.FEED.label(), 1);
    }

    @Transactional
    public Feed saveFeed(String content, Profile profile) {
        Feed feed = Feed.builder()
                .profile(profile)
                .content(content)
                .isPresent(true)
                .build();

        return feedRepository.save(feed);
    }

    @Transactional
    public void saveFeedImages(List<File> images, Feed feed) {
        List<FeedImage> feedImages = new ArrayList<>();
        try {
            for (int i = 0; i < images.size(); i++) {
                Image originalImage = imageService.saveImage(images.get(i));

                FeedImage feedImage = FeedImage.builder()
                        .imageId(originalImage)
                        .feedId(feed)
                        .isPresent(true)
                        .isCover(false)
                        .imageIndex(i)
                        .build();
                feedImages.add(feedImage);
            }
            File coverImageFile = imageService.resizeImage(images.get(0), "cover_", 250, 250);
            Image coverImage = imageService.saveImage(coverImageFile);
            FeedImage feedCoverImage = FeedImage.builder()
                    .imageId(coverImage)
                    .feedId(feed)
                    .isPresent(true)
                    .isCover(true)
                    .imageIndex(0)
                    .build();
            feedImages.add(feedCoverImage);
            feedImageRepository.saveAll(feedImages);

        } catch (Exception e) {
            throw new RuntimeException("이미지 처리에 실패하였습니다.");
        }
    }

    @Transactional
    public void saveHashTags(Set<String> hashTags, Feed feed) {
        List<HashTag> hashTagEntities = new ArrayList<>();

        if (hashTags == null) {
            return;
        }

        for (String hashTag : hashTags) {
            HashTag tag = HashTag.builder()
                    .feed(feed)
                    .tag(hashTag)
                    .isPresent(true)
                    .build();
            hashTagEntities.add(tag);
        }

        hashTagRepository.saveAll(hashTagEntities);
    }


    @Transactional
    @Override
    public void delete(DeleteFeedServiceDto dto) {
        Profile profile = profileRepository.findByUserId(dto.getUserId())
                .orElseThrow(() -> new ProfileException(ProfileExceptionType.NOT_FOUND));

        Feed feed = feedRepository.findById(dto.getFeedId())
                .orElseThrow(() -> new FeedException(FeedExceptionType.NOT_FOUND));

        if (!feed.getProfile().getId().equals(profile.getId())) {
            throw new FeedException(FeedExceptionType.UNAUTHORIZED);
        }

        feed.delete();

        // 프로필에 피드 수 감소
        statisticsService.updateStatistics(profile.getId(), StatisticType.FEED.label(), -1);
    }


    @Transactional
    @Override
    public void modify(ModifyFeedServiceDto dto) {
        Profile profile = profileRepository.findByUserId(dto.getUserId())
                .orElseThrow(() -> new ProfileException(ProfileExceptionType.NOT_FOUND));

        Feed feed = feedRepository.findById(dto.getFeedId())
                .orElseThrow(() -> new FeedException(FeedExceptionType.NOT_FOUND));

        if (!feed.getProfile().getId().equals(profile.getId())) {
            throw new FeedException(FeedExceptionType.UNAUTHORIZED);
        }

        if (dto.getHashTags() != null) {
            // hashtag 다 지우는거
            hashTagRepository.deleteByFeedId(feed.getId());

            saveHashTags(dto.getHashTags(), feed);
        }

        feed.modify(dto.getContent());
    }
}