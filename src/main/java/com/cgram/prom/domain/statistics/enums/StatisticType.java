package com.cgram.prom.domain.statistics.enums;

public enum StatisticType {
    FEED("FEED")
    , COMMENT("COMMENT")
    , FEED_LIKE("FEED_LIKE")
    , COMMENT_LIKE("COMMENT_LIKE")
    , FOLLOWING("FOLLOWING") // 내'가' 팔로우 하는 사람들
    , FOLLOWER("FOLLOWER") // 나'를' 팔로우 하는 사람들
    ;

    private final String label;

    StatisticType(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
