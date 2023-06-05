package com.cgram.prom.domain.statistics.enums;

public enum StatisticType {
    FEED("FEED")
    , COMMENT("COMMENT")
    , FEED_LIKE("FEED_LIKE")
    , COMMENT_LIKE("COMMENT_LIKE")
    ;

    private final String label;

    StatisticType(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
