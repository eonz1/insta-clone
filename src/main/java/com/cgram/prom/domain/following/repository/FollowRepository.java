package com.cgram.prom.domain.following.repository;

import com.cgram.prom.domain.following.domain.Follow;
import com.cgram.prom.domain.following.domain.FollowId;
import com.cgram.prom.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, FollowId> {

    Long countByUser(User user);

    Long countByFollowedUser(User user);
}
