package com.cgram.prom.domain.feed.repository;

import com.cgram.prom.domain.feed.domain.Feed;
import com.cgram.prom.domain.user.domain.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedRepository extends JpaRepository<Feed, UUID> {

    Long countByUser(User build);
}
