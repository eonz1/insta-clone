package com.cgram.prom.domain.feed.repository;

import com.cgram.prom.domain.feed.domain.hashtag.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HashTagRepository extends JpaRepository<HashTag, Long> {

}
