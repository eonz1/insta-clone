# 회원
CREATE TABLE `user`
(
    `id`         binary(16) NOT NULL,
    `email`      varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `is_present` bit(1)     NOT NULL,
    `password`   varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

# 이미지
CREATE TABLE `image`
(
    `id`         binary(16) NOT NULL,
    `created_at` datetime(6)                             DEFAULT NULL,
    `is_present` bit(1)     NOT NULL,
    `path`       varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

# 프로필
CREATE TABLE `profile`
(
    `id`        binary(16) NOT NULL,
    `intro`     varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `is_public` bit(1)     NOT NULL,
    `image_id`  binary(16)                              DEFAULT NULL,
    `user_id`   binary(16)                              DEFAULT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    FOREIGN KEY (`image_id`) REFERENCES `image` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

# 팔로우
CREATE TABLE `follow`
(
    `followed_id` binary(16) NOT NULL,
    `profile_id`  binary(16) NOT NULL,
    `created_at`  datetime(6) DEFAULT NULL,
    `is_present`  bit(1)     NOT NULL,
    PRIMARY KEY (`followed_id`, `profile_id`),
    FOREIGN KEY (`followed_id`) REFERENCES `profile` (`id`),
    FOREIGN KEY (`profile_id`) REFERENCES `profile` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

# 헬로
CREATE TABLE `hello`
(
    `id`   bigint NOT NULL AUTO_INCREMENT,
    `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;


# 리프레시 토큰
CREATE TABLE `refresh_token`
(
    `id`            bigint NOT NULL AUTO_INCREMENT,
    `created_at`    datetime(6)                             DEFAULT NULL,
    `refresh_token` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `user_id`       binary(16)                              DEFAULT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

# 통계
CREATE TABLE `statistics`
(
    `id`     bigint NOT NULL AUTO_INCREMENT,
    `counts` int    NOT NULL,
    `type`   varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `uuid`   binary(16)                              DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;


# 검증 코드
CREATE TABLE `verification_code`
(
    `id`              bigint NOT NULL AUTO_INCREMENT,
    `code`            varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `email`           varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `expiration_date` datetime(6)                             DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

# 피드
CREATE TABLE `feed`
(
    `id`          binary(16) NOT NULL,
    `content`     varchar(2200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `created_at`  datetime(6)                              DEFAULT NULL,
    `is_present`  bit(1)     NOT NULL,
    `modified_at` datetime(6)                              DEFAULT NULL,
    `profile_id`  binary(16)                               DEFAULT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`profile_id`) REFERENCES `profile` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

# 피드 이미지
CREATE TABLE `feed_image`
(
    `feed_id`    binary(16) NOT NULL,
    `image_id`   binary(16) NOT NULL,
    `is_cover`   bit(1)     NOT NULL,
    `is_present` bit(1)     NOT NULL,
    PRIMARY KEY (`feed_id`, `image_id`),
    FOREIGN KEY (`feed_id`) REFERENCES `feed` (`id`),
    FOREIGN KEY (`image_id`) REFERENCES `image` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

# 피드 좋아요
CREATE TABLE `feed_like`
(
    `feed_id`    binary(16) NOT NULL,
    `profile_id` binary(16) NOT NULL,
    `is_present` bit(1)     NOT NULL,
    PRIMARY KEY (`feed_id`, `profile_id`),
    FOREIGN KEY (`feed_id`) REFERENCES `feed` (`id`),
    FOREIGN KEY (`profile_id`) REFERENCES `profile` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

# 해시태그
CREATE TABLE `hash_tag`
(
    `id`      bigint NOT NULL AUTO_INCREMENT,
    `tag`     varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `feed_id` binary(16)                              DEFAULT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`feed_id`) REFERENCES `feed` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

# 댓글
CREATE TABLE `comment`
(
    `id`          binary(16) NOT NULL,
    `content`     varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `created_at`  datetime(6)                             DEFAULT NULL,
    `is_present`  bit(1)     NOT NULL,
    `modified_at` datetime(6)                             DEFAULT NULL,
    `feed_id`     binary(16)                              DEFAULT NULL,
    `profile_id`  binary(16)                              DEFAULT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`profile_id`) REFERENCES `profile` (`id`),
    FOREIGN KEY (`feed_id`) REFERENCES `feed` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

# 댓글 좋아요
CREATE TABLE `comment_like`
(
    `comment_id` binary(16) NOT NULL,
    `profile_id` binary(16) NOT NULL,
    `is_present` bit(1)     NOT NULL,
    PRIMARY KEY (`comment_id`, `profile_id`),
    FOREIGN KEY (`profile_id`) REFERENCES `profile` (`id`),
    FOREIGN KEY (`comment_id`) REFERENCES `comment` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;



