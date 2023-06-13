package com.cgram.prom.domain.feed.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.cgram.prom.domain.comment.domain.Comment;
import com.cgram.prom.domain.comment.repository.CommentRepository;
import com.cgram.prom.domain.feed.domain.Feed;
import com.cgram.prom.domain.feed.domain.hashtag.HashTag;
import com.cgram.prom.domain.feed.dto.FeedDTO;
import com.cgram.prom.domain.feed.request.GetFeedsRepoDto;
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

    @Autowired
    FeedImageRepository feedImageRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    CommentRepository commentRepository;

    @BeforeEach
    void setup() {
        followRepository.deleteAll();
        hashTagRepository.deleteAll();
        feedRepository.deleteAll();
        imageRepository.deleteAll();
        profileRepository.deleteAll();
        userRepository.deleteAll();

    }

    private void saveComment(Profile profile, Feed feed, String content) {
        Comment comment = Comment.builder()
            .profile(profile)
            .content(content)
            .feed(feed)
            .build();
        commentRepository.save(comment);

        saveStatistics(feed.getId(), StatisticType.COMMENT.label(), 1);
    }

    private Follow saveFollow(Profile userProfile, Profile followedProfile) {
        Follow follow = Follow.builder()
                .profileId(userProfile)
                .followedId(followedProfile)
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
    public void getFeedsByHashtag() throws Exception {
        // given
        User user = saveUser("test@test.com", "password123!!!");
        Profile userProfile = saveProfile(user);

        for (int i = 0; i < 10; i++) {
            Feed feed = saveFeed("피드" + (i + 1), userProfile);
            saveHashTag("#tag", feed);
            saveHashTag("#tag1", feed);
            saveComment(userProfile, feed, "댓글"+(i+1));

            if (i % 2 == 0) {
                saveHashTag("#태그", feed);
            }
        }

        GetFeedsRepoDto dto = GetFeedsRepoDto.builder().tag("#tag").limit(5).build();

        // when
        List<FeedDTO> allFeedsByHashTag = feedQueryRepository.getFeedsByHashTag(dto);

        // then
        assertThat(allFeedsByHashTag.size()).isEqualTo(6);
        assertThat(allFeedsByHashTag.stream().map(FeedDTO::getProfileImagePath).allMatch(s -> s.equals("path"))).isTrue();
        assertThat(allFeedsByHashTag.stream().map(FeedDTO::getCommentCount).allMatch(i -> i.equals(1))).isTrue();
    }

    @Test
    @DisplayName("해시태그로 피드 조회하기 - 더보기")
    public void getFeedsByHashtagNextId() throws Exception {
        // given
        User user = saveUser("test@test.com", "password123!!!");
        Profile userProfile = saveProfile(user);

        for (int i = 0; i < 10; i++) {
            Feed feed = saveFeed("피드" + (i + 1), userProfile);
            saveHashTag("#tag", feed);
            saveHashTag("#tag1", feed);
            saveComment(userProfile, feed, "댓글"+(i+1));

            if (i % 2 == 0) {
                saveHashTag("#태그", feed);
            }
        }

        List<FeedDTO> feedDTOList = feedQueryRepository.getFeedsByHashTag(
            GetFeedsRepoDto.builder()
                .tag("#tag")
                .limit(5)
                .build());
        String nextId = feedDTOList.get(5).getId().toString();

        GetFeedsRepoDto dto = GetFeedsRepoDto.builder()
            .profileId(userProfile.getId().toString())
            .limit(5)
            .cursor(nextId)
            .build();

        // when
        List<FeedDTO> allFeedsByHashTag = feedQueryRepository.getFeedsByHashTag(dto);

        // then
        assertThat(allFeedsByHashTag.size()).isEqualTo(5);
        assertThat(allFeedsByHashTag.stream().map(FeedDTO::getProfileImagePath).allMatch(s -> s.equals("path"))).isTrue();
        assertThat(allFeedsByHashTag.stream().map(FeedDTO::getCommentCount).allMatch(i -> i.equals(1))).isTrue();
    }

    @Test
    @DisplayName("내 프로필 피드 조회하기")
    public void getFeedsByMyProfile() throws Exception {
        // given
        User user = saveUser("test@test.com", "password123!!!");
        Profile userProfile = saveProfile(user);

        for (int i = 0; i < 10; i++) {
            Feed feed = saveFeed("내 피드" + (i + 1), userProfile);
            saveHashTag("#tag", feed);
            saveComment(userProfile, feed, "댓글"+(i+1));
        }

        GetFeedsRepoDto dto = GetFeedsRepoDto.builder()
                .profileId(userProfile.getId().toString())
                .limit(5)
                .build();

        // when
        List<FeedDTO> allFeedsByMyProfile = feedQueryRepository.getFeedsByUser(dto,
                LocalDateTime.now().minusDays(3));

        // then
        assertThat(allFeedsByMyProfile.size()).isEqualTo(6);
        assertThat(allFeedsByMyProfile.stream().map(FeedDTO::getContent)
                .allMatch(s -> s.contains("내 피드"))).isTrue();
        assertThat(allFeedsByMyProfile.stream().map(FeedDTO::getProfileImagePath).allMatch(s -> s.equals("path"))).isTrue();
        assertThat(allFeedsByMyProfile.stream().map(FeedDTO::getCommentCount).allMatch(i -> i.equals(1))).isTrue();
    }

    @Test
    @DisplayName("내가 팔로잉한 사람 피드 조회하기 - 더보기")
    public void getFeedsByMyProfileNextId() throws Exception {
        // given
        User user = saveUser("test@test.com", "password123!!!");
        Profile userProfile = saveProfile(user);

        for (int i = 0; i < 10; i++) {
            Feed feed = saveFeed("내 피드" + (i + 1), userProfile);
            saveHashTag("#tag", feed);
            saveComment(userProfile, feed, "댓글"+(i+1));
        }

        List<FeedDTO> feedDTOList = feedQueryRepository.getFeedsByUser(
            GetFeedsRepoDto.builder()
                .profileId(userProfile.getId().toString())
                .limit(5)
                .build(),
            LocalDateTime.now().minusDays(3));
        String nextId = feedDTOList.get(5).getId().toString();

        GetFeedsRepoDto dto = GetFeedsRepoDto.builder()
            .profileId(userProfile.getId().toString())
            .limit(5)
            .cursor(nextId)
            .build();

        // when
        List<FeedDTO> allFeedsByMyProfile = feedQueryRepository.getFeedsByUser(dto,
            LocalDateTime.now().minusDays(3));

        // then
        assertThat(allFeedsByMyProfile.size()).isEqualTo(5);
        assertThat(allFeedsByMyProfile.stream().map(FeedDTO::getContent)
            .allMatch(s -> s.contains("내 피드"))).isTrue();
        assertThat(allFeedsByMyProfile.stream().map(FeedDTO::getProfileImagePath).allMatch(s -> s.equals("path"))).isTrue();
        assertThat(allFeedsByMyProfile.stream().map(FeedDTO::getCommentCount).allMatch(i -> i.equals(1))).isTrue();
    }

    @Test
    @DisplayName("내가 팔로잉한 사람 피드 조회하기")
    public void getFeedsByMyFollowingProfiles() throws Exception {
        // given
        User user = saveUser("test@test.com", "password123!!!");
        User followUser = saveUser("follow1@test.com", "password123!!!");
        User followUser2 = saveUser("follow2@test.com", "password123!!!");

        Profile userProfile = saveProfile(user);
        Profile followProfile = saveProfile(followUser);
        Profile followProfile2 = saveProfile(followUser2);

        saveFollow(userProfile, followProfile);
        saveFollow(userProfile, followProfile2);

        for (int i = 0; i < 4; i++) {
            Feed feed = saveFeed("내 피드" + (i + 1), userProfile);
            Feed followedFeed = saveFeed("팔로우한 사람 피드" + (i + 1), followProfile);

            if (i % 2 == 0) {
                Feed followedFeed2 = saveFeed("팔로우한 사람2 피드" + (i + 1), followProfile2);
                saveComment(userProfile, followedFeed2, "팔로우한 사람2 피드 댓글"+(i+1));
            }

            saveHashTag("tag", feed);
            saveHashTag("follow tag", feed);
            saveHashTag("tag2", followedFeed);
            saveComment(userProfile, followedFeed, "댓글"+(i+1));
        }

        GetFeedsRepoDto dto = GetFeedsRepoDto.builder()
                .profileId(userProfile.getId().toString())
                .limit(5)
                .build();

        // when
        List<FeedDTO> allFeedsByMyFollowings = feedQueryRepository.getFeedsByMyFollowings(dto,
                LocalDateTime.now().minusDays(3));

        // then
        assertThat(allFeedsByMyFollowings.size()).isEqualTo(6);
        assertThat(allFeedsByMyFollowings.stream().map(FeedDTO::getContent)
                .allMatch(s -> s.contains("팔로우한 사람"))).isTrue();
        assertThat(allFeedsByMyFollowings.stream().map(FeedDTO::getProfileImagePath).allMatch(s -> s.equals("path"))).isTrue();
        assertThat(allFeedsByMyFollowings.stream().map(FeedDTO::getCommentCount).allMatch(i -> i.equals(1))).isTrue();
    }

    @Test
    @DisplayName("내가 팔로잉한 사람 피드 조회하기 - 더보기")
    public void getFeedsByMyFollowingProfilesNextId() throws Exception {
        // given
        User user = saveUser("test@test.com", "password123!!!");
        User followUser = saveUser("follow1@test.com", "password123!!!");
        User followUser2 = saveUser("follow2@test.com", "password123!!!");

        Profile userProfile = saveProfile(user);
        Profile followProfile = saveProfile(followUser);
        Profile followProfile2 = saveProfile(followUser2);

        saveFollow(userProfile, followProfile);
        saveFollow(userProfile, followProfile2);

        for (int i = 0; i < 5; i++) {
            Feed feed = saveFeed("내 피드" + (i + 1), userProfile);
            Feed followFeed = saveFeed("팔로우한 사람 피드" + (i + 1), followProfile);

            if (i % 2 == 0) {
                Feed followFeed2 = saveFeed("팔로우한 사람2 피드" + (i + 1), followProfile2);
                saveComment(userProfile, followFeed2, "팔로우한 사람2 피드 댓글"+(i+1));
            }

            saveHashTag("tag", feed);
            saveHashTag("follow tag", feed);
            saveHashTag("tag2", followFeed);
            saveComment(userProfile, followFeed, "댓글"+(i+1));
        }

        List<FeedDTO> feedDTOList = feedQueryRepository.getFeedsByMyFollowings(
            GetFeedsRepoDto.builder()
                .profileId(userProfile.getId().toString())
                .limit(3)
                .build()
            , LocalDateTime.now().minusDays(3));
        String nextId = feedDTOList.get(3).getId().toString();

        // when
        GetFeedsRepoDto dto = GetFeedsRepoDto.builder()
            .profileId(userProfile.getId().toString())
            .limit(3)
            .cursor(nextId)
            .build();
        List<FeedDTO> allFeedsByMyFollowings = feedQueryRepository.getFeedsByMyFollowings(dto,
            LocalDateTime.now().minusDays(3));

        // then
        assertThat(allFeedsByMyFollowings.size()).isEqualTo(4);
        assertThat(allFeedsByMyFollowings.stream().map(FeedDTO::getContent)
            .allMatch(s -> s.contains("팔로우한 사람"))).isTrue();
        assertThat(allFeedsByMyFollowings.stream().map(FeedDTO::getProfileImagePath).allMatch(s -> s.equals("path"))).isTrue();
        assertThat(allFeedsByMyFollowings.stream().map(FeedDTO::getCommentCount).allMatch(i -> i.equals(1))).isTrue();
    }
}