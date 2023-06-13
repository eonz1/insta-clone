package com.cgram.prom.domain.feed.service;

import com.cgram.prom.domain.feed.domain.Feed;
import com.cgram.prom.domain.feed.domain.hashtag.HashTag;
import com.cgram.prom.domain.feed.domain.image.FeedImage;
import com.cgram.prom.domain.feed.exception.FeedException;
import com.cgram.prom.domain.feed.exception.FeedExceptionType;
import com.cgram.prom.domain.feed.repository.FeedImageRepository;
import com.cgram.prom.domain.feed.repository.FeedRepository;
import com.cgram.prom.domain.feed.repository.HashTagRepository;
import com.cgram.prom.domain.feed.request.DeleteFeedServiceDto;
import com.cgram.prom.domain.feed.request.ModifyFeedServiceDto;
import com.cgram.prom.domain.feed.request.PostFeedServiceDto;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedCommandServiceImpl implements FeedCommandService {

    private final ProfileRepository profileRepository;
    private final FeedRepository feedRepository;
    private final ImageService imageService;
    private final FeedImageRepository feedImageRepository;
    private final HashTagRepository hashTagRepository;
    private final StatisticsService statisticsService;

    @Transactional
    @Override
    public void post(PostFeedServiceDto dto) {
        Profile profile = profileRepository.findByUserId(dto.getId())
            .orElseThrow(() -> new ProfileException(ProfileExceptionType.NOT_FOUND));

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
