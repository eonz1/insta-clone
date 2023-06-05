package com.cgram.prom.domain.feed.repository;

import com.cgram.prom.domain.feed.domain.like.FeedLike;
import com.cgram.prom.domain.feed.domain.like.FeedLikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedLikeRepository extends JpaRepository<FeedLike, FeedLikeId> {

}
