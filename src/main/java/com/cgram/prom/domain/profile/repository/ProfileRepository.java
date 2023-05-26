package com.cgram.prom.domain.profile.repository;

import com.cgram.prom.domain.profile.domain.Profile;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, UUID> {

    Optional<Profile> findByUserId(UUID id);

    @Query(value = "SELECT user.email, image.path as imagePath, p.intro, p.is_public as isPublic"
        + ", follower.count as followerCount, following.count as followingCount, feed.count as feedCount"
        + ", EXISTS(SELECT * FROM follow WHERE HEX(user_id) = REPLACE(:loginUserId, '-', '') and HEX(followed_id) = REPLACE(:userId, '-', '')) as isFollowing"
        + "    FROM profile p"
        + "    INNER JOIN user ON p.user_id = user.id"
        + "    LEFT JOIN (SELECT id, path, is_present, created_at FROM image WHERE is_present = true) AS image ON p.image_id = image.id"
        + "    LEFT JOIN (SELECT user_id, count(*) as count FROM feed WHERE is_present=true and HEX(user_id) = REPLACE(:userId, '-', '') GROUP BY user_id) AS feed ON feed.user_id = p.user_id"
        + "    LEFT JOIN (SELECT user_id, count(*) as count FROM follow WHERE is_present=true and HEX(user_id) = REPLACE(:userId, '-', '') GROUP BY user_id) AS following ON following.user_id = p.user_id"
        + "    LEFT JOIN (SELECT followed_id, count(*) as count FROM follow WHERE is_present=true and HEX(followed_id) = REPLACE(:userId, '-', '') GROUP BY followed_id) AS follower ON follower.followed_id = p.user_id"
        + "    WHERE user.is_present = true and HEX(p.user_id) = REPLACE(:userId, '-', '')", nativeQuery = true)
    ProfileWithCounts getProfileWithCountsByUserId(@Param("userId") String id, @Param("loginUserId") String loginUserId);

    interface ProfileWithCounts {
        String getEmail();
        String getImagePath();
        String getIntro();
        boolean getIsPublic();
        Long getFeedCount();
        Long getFollowerCount();
        Long getFollowingCount();
        int getIsFollowing();
    }
}
