package com.cgram.prom.domain.feed.repository;

import com.cgram.prom.domain.feed.domain.image.FeedImage;
import com.cgram.prom.domain.feed.domain.image.FeedImageId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedImageRepository extends JpaRepository<FeedImage, FeedImageId> {

}
