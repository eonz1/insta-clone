package com.cgram.prom.domain.feed.repository;

import com.cgram.prom.domain.feed.domain.hashtag.HashTag;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HashTagRepository extends JpaRepository<HashTag, Long> {

    @Modifying
    @Query("delete from HashTag as hashTag where hashTag.feed.id = :feedId")
    int deleteByFeedId(@Param("feedId") UUID feedId);
}
