package com.cgram.prom.domain.feed.repository;

import com.cgram.prom.domain.feed.domain.Feed;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedRepository extends JpaRepository<Feed, UUID> {

}
