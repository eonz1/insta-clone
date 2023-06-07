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

    @Query(value = """
        SELECT
            user.email, image.path AS imagePath, p.intro, p.is_public AS isPublic,
            follower.count AS followerCount, following.count AS followingCount, feed.count AS feedCount,
            EXISTS(SELECT * FROM follow WHERE profile_id = :loginProfileId AND followed_id = :profileId) AS isFollowing
        FROM profile p
        INNER JOIN user ON p.user_id = user.id
        LEFT JOIN (SELECT id, path, is_present, created_at
                    FROM image WHERE is_present = true) AS image ON p.image_id = image.id
        LEFT JOIN (SELECT profile_id, count(*) AS count
                    FROM feed WHERE is_present=true AND profile_id = :profileId
                    GROUP BY profile_id) AS feed ON feed.profile_id = p.id
        LEFT JOIN (SELECT profile_id, count(*) AS count
                    FROM follow WHERE is_present=true AND profile_id = :profileId
                    GROUP BY profile_id) AS following ON following.profile_id = p.id
        LEFT JOIN (SELECT followed_id, count(*) AS count
                    FROM follow WHERE is_present=true AND followed_id = :profileId
                    GROUP BY followed_id) AS follower ON follower.followed_id = p.id
        WHERE
            user.is_present = true
            AND p.id = :profileId
        """, nativeQuery = true)
    ProfileWithCounts getProfileWithCountsByProfileId(@Param("profileId") UUID id,
        @Param("loginProfileId") UUID loginProfileId);

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
