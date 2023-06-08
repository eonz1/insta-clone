package com.cgram.prom.domain.feed.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.cgram.prom.domain.feed.domain.Feed;
import com.cgram.prom.domain.feed.domain.hashtag.HashTag;
import com.cgram.prom.domain.feed.dto.FeedDTO;
import com.cgram.prom.domain.feed.request.GetFeedsServiceDto;
import com.cgram.prom.domain.following.domain.Follow;
import com.cgram.prom.domain.following.repository.FollowRepository;
import com.cgram.prom.domain.image.domain.Image;
import com.cgram.prom.domain.image.repository.ImageRepository;
import com.cgram.prom.domain.profile.domain.Profile;
import com.cgram.prom.domain.profile.repository.ProfileRepository;
import com.cgram.prom.domain.statistics.domain.Statistics;
import com.cgram.prom.domain.statistics.enums.StatisticType;
import com.cgram.prom.domain.statistics.repository.StatisticsRepository;
import com.cgram.prom.domain.user.domain.User;
import com.cgram.prom.domain.user.repository.UserRepository;
import com.cgram.prom.global.config.JpaAuditingConfig;
import com.cgram.prom.global.config.QuerydslTestConfig;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("dev")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QuerydslTestConfig.class, FeedQueryRepository.class, JpaAuditingConfig.class})
class FeedQueryRepositoryTest {

    @Autowired
    private FeedQueryRepository feedQueryRepository;

    @Autowired
    FeedRepository feedRepository;

    @Autowired
    HashTagRepository hashTagRepository;

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    FollowRepository followRepository;

    @Autowired
    StatisticsRepository statisticsRepository;

    @Autowired
    UserRepository userRepository;

//    @Autowired
//    FeedImageRepository feedImageRepository;

    @Autowired
    ImageRepository imageRepository;

    @BeforeEach
    void setup() {
        followRepository.deleteAll();
        hashTagRepository.deleteAll();
        feedRepository.deleteAll();
        imageRepository.deleteAll();
        profileRepository.deleteAll();
        userRepository.deleteAll();

    }

    private Follow saveFollow(Profile followProfile, Profile userProfile) {
        Follow follow = Follow.builder()
                .profileId(userProfile)
                .followedId(followProfile)
                .isPresent(true)
                .build();

        return followRepository.save(follow);
    }

    private void saveStatistics(UUID uuid, String type, int count) {
        Statistics statistics = Statistics.builder()
                .uuid(uuid)
                .counts(count)
                .type(type)
                .build();
        statisticsRepository.save(statistics);
    }

    private Profile saveProfile(User user) {
        Profile profile = Profile.builder()
                .user(user)
                .image(saveImage())
                .build();

        return profileRepository.save(profile);
    }

    private User saveUser(String email, String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encode = encoder.encode(password);
        User user = User.builder().password(encode).email(email).build();
        return userRepository.save(user);
    }

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
                .isPresent(true)
                .build();

        return hashTagRepository.save(hashTag);
    }

    private Image saveImage() {
        Image image = Image.builder()
                .path("path")
                .isPresent(true)
                .build();

        return imageRepository.save(image);
    }

    @Test
    @DisplayName("해시태그로 피드 조회하기")
    @Commit
    public void getFeedsByHashtag() throws Exception {
        // given
        List<HashTag> hashTags = new ArrayList<>();

        User user = saveUser("test@test.com", "password123!!!");
        User followUser = saveUser("follow1@test.com", "password123!!!");

        Profile userProfile = saveProfile(user);
        Profile followProfile = saveProfile(followUser);

        saveFollow(followProfile, userProfile);
        saveStatistics(userProfile.getId(), StatisticType.FOLLOWING.label(), 2);

        for (int i = 0; i < 10; i++) {
            Feed feed = saveFeed("피드" + (i + 1), userProfile);
            saveHashTag("#tag", feed);
            saveHashTag("#tag1", feed);

            if (i % 2 == 0) {
                saveHashTag("#태그", feed);
            }
        }

        GetFeedsServiceDto dto = GetFeedsServiceDto.builder().tag("#tag").limit(5).build();

        // when
        List<FeedDTO> allFeedsByHashTag = feedQueryRepository.getAllFeedsByHashTag(dto,
                LocalDateTime.now().minusDays(3));

        // then
        assertThat(allFeedsByHashTag.size()).isEqualTo(6);
        assertThat(allFeedsByHashTag.stream().map(FeedDTO::getProfileImagePath).allMatch(s -> s.equals("path"))).isTrue();

    }

    @Test
    @DisplayName("내 프로필 피드 조회하기")
    @Commit
    public void getFeedsByMyProfile() throws Exception {
        // given
        List<HashTag> hashTags = new ArrayList<>();

        User user = saveUser("test@test.com", "password123!!!");
        User followUser = saveUser("follow1@test.com", "password123!!!");

        Profile userProfile = saveProfile(user);
        Profile followProfile = saveProfile(followUser);

        saveFollow(followProfile, userProfile);
        saveStatistics(userProfile.getId(), StatisticType.FOLLOWING.label(), 2);

        for (int i = 0; i < 10; i++) {
            Feed feed = saveFeed("내 피드" + (i + 1), userProfile);
            Feed followedFeed = saveFeed("팔로우한 사람 피드" + (i + 1), followProfile);

            saveHashTag("#tag", feed);
        }

        GetFeedsServiceDto dto = GetFeedsServiceDto.builder()
                .profileId(userProfile.getId().toString())
                .limit(5)
                .build();

        // when
        List<FeedDTO> allFeedsByMyProfile = feedQueryRepository.getAllFeedsByMyProfile(dto,
                LocalDateTime.now().minusDays(3));

        // then
        assertThat(allFeedsByMyProfile.size()).isEqualTo(6);
        assertThat(allFeedsByMyProfile.stream().map(FeedDTO::getContent)
                .allMatch(s -> s.contains("내 피드"))).isTrue();
        assertThat(allFeedsByMyProfile.stream().map(FeedDTO::getProfileImagePath).allMatch(s -> s.equals("path"))).isTrue();
    }

    @Test
    @DisplayName("내가 팔로잉한 사람 피드 조회하기")
    @Commit
    public void getFeedsByMyFollowingProfiles() throws Exception {
        // given
        User user = saveUser("test@test.com", "password123!!!");
        User followUser = saveUser("follow1@test.com", "password123!!!");
        User followUser2 = saveUser("follow2@test.com", "password123!!!");

        Profile userProfile = saveProfile(user);
        Profile followProfile = saveProfile(followUser);
        Profile followProfile2 = saveProfile(followUser2);

        saveFollow(followProfile, userProfile);
        saveFollow(followProfile2, userProfile);
        saveStatistics(userProfile.getId(), StatisticType.FOLLOWING.label(), 2);

        for (int i = 0; i < 4; i++) {
            Feed feed = saveFeed("내 피드" + (i + 1), userProfile);
            Feed followedFeed = saveFeed("팔로우한 사람 피드" + (i + 1), followProfile);

            if (i % 2 == 0) {
                Feed followedFeed2 = saveFeed("팔로우한 사람2 피드" + (i + 1), followProfile2);
            }

            HashTag tag = saveHashTag("tag", feed);
            saveHashTag("follow tag", feed);
            saveHashTag("tag2", followedFeed);
        }

        GetFeedsServiceDto dto = GetFeedsServiceDto.builder()
                .profileId(userProfile.getId().toString())
                .limit(5)
                .build();

        // when
        List<FeedDTO> allFeedsByMyFollowings = feedQueryRepository.getAllFeedsByMyFollowings(dto,
                LocalDateTime.now().minusDays(3));

        assertThat(allFeedsByMyFollowings.size()).isEqualTo(6);
        assertThat(allFeedsByMyFollowings.stream().map(FeedDTO::getContent)
                .allMatch(s -> s.contains("팔로우한 사람"))).isTrue();
        assertThat(allFeedsByMyFollowings.stream().map(FeedDTO::getProfileImagePath).allMatch(s -> s.equals("path"))).isTrue();

    }

    // TODO: 2023/06/08  getAllFeedsByMyFollowings dto에 nextId 포함되어 있는 경우
    // TODO: 2023/06/08  getAllFeedsByMyProfile dto에 nextId 포함되어 있는 경우
    // TODO: 2023/06/08  getAllFeedsByHashTag dto에 nextId 포함되어 있는 경우
}