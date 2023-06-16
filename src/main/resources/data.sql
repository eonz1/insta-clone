insert into `user`(id, email, is_present, password)
values (UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840000', '-', '')), 'test1@test.com', true,
        '$2a$10$IL9MPRCTP/NLTQSOi8.ft.cGTDXW7XLYdMAFpDioZDAz4S.MlbB9e');
insert into image(id, created_at, is_present, path)
values (UNHEX(REPLACE('da26891b-88b3-17f7-8188-b724f3e30000', '-', '')), now(), true,
        '/Users/eonji/Development/Projects/prom/src/main/resources/static');
insert into profile(id, intro, is_public, image_id, user_id)
values (UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840001', '-', '')), '프로필 소개1', true,
        UNHEX(REPLACE('da26891b-88b3-17f7-8188-b724f3e30000', '-', '')),
        UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840000', '-', '')));

insert into `user`(id, email, is_present, password)
values (UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840006', '-', '')), 'test2@test.com', true,
        '$2a$10$IL9MPRCTP/NLTQSOi8.ft.cGTDXW7XLYdMAFpDioZDAz4S.MlbB9e');
insert into profile(id, intro, is_public, image_id, user_id)
values (UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840007', '-', '')), '프로필 소개2', true, null,
        UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840006', '-', '')));

insert into `user`(id, email, is_present, password)
values (UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840008', '-', '')), 'test3@test.com', true,
        '$2a$10$IL9MPRCTP/NLTQSOi8.ft.cGTDXW7XLYdMAFpDioZDAz4S.MlbB9e');
insert into profile(id, intro, is_public, image_id, user_id)
values (UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840009', '-', '')), '프로필 소개3', true, null,
        UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840008', '-', '')));

/* 팔로잉 */
insert into follow(followed_id, profile_id, created_at, is_present)
values (UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840007', '-', '')),
        UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840001', '-', '')), now(), true);
insert into statistics(counts, type, uuid)
values (1, 'FOLLOWING', UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840001', '-', '')));

/* 피드 7개, 첫피드에만 정보있음 */
insert into feed(id, content, created_at, is_present, modified_at, profile_id)
values (UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840002', '-', '')), '피드 내용1', now(), true,
        now(), UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840007', '-', '')));
insert into feed(id, content, created_at, is_present, modified_at, profile_id)
values (UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840020', '-', '')), '피드 내용2',
        STR_TO_DATE('20230610 182238', '%Y%m%d %H%i%s'), true, now(),
        UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840007', '-', '')));
insert into feed(id, content, created_at, is_present, modified_at, profile_id)
values (UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840021', '-', '')), '피드 내용3',
        STR_TO_DATE('20230610 182238', '%Y%m%d %H%i%s'), true, now(),
        UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840007', '-', '')));
insert into feed(id, content, created_at, is_present, modified_at, profile_id)
values (UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840022', '-', '')), '피드 내용4',
        STR_TO_DATE('20230610 182238', '%Y%m%d %H%i%s'), true, now(),
        UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840007', '-', '')));
insert into feed(id, content, created_at, is_present, modified_at, profile_id)
values (UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840023', '-', '')), '피드 내용5',
        STR_TO_DATE('20230610 182238', '%Y%m%d %H%i%s'), true, now(),
        UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840009', '-', '')));
insert into feed(id, content, created_at, is_present, modified_at, profile_id)
values (UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840024', '-', '')), '피드 내용6',
        STR_TO_DATE('20230610 182238', '%Y%m%d %H%i%s'), true, now(),
        UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840007', '-', '')));
insert into feed(id, content, created_at, is_present, modified_at, profile_id)
values (UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840025', '-', '')), '피드 내용7',
        STR_TO_DATE('20230610 182238', '%Y%m%d %H%i%s'), true, now(),
        UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840007', '-', '')));

/* 사진 2개, 썸네일 1개 */
insert into feed_image(feed_id, image_id, is_cover, is_present)
values (UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840002', '-', '')),
        UNHEX(REPLACE('da26891b-88b3-17f7-8188-b724f3e30000', '-', '')), true, true);
insert into image(id, created_at, is_present, path)
values (UNHEX(REPLACE('da26891b-889f-1673-8188-9f0679a90001', '-', '')), now(), true,
        '/Users/eonji/Development/Projects/prom/src/main/resources/static');
insert into feed_image(feed_id, image_id, is_cover, is_present)
values (UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840002', '-', '')),
        UNHEX(REPLACE('da26891b-889f-1673-8188-9f0679a90001', '-', '')), false, true);
insert into image(id, created_at, is_present, path)
values (UNHEX(REPLACE('da26891b-889f-1673-8188-9f0679a90002', '-', '')), now(), true,
        '/Users/eonji/Development/Projects/prom/src/main/resources/static');
insert into feed_image(feed_id, image_id, is_cover, is_present)
values (UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840002', '-', '')),
        UNHEX(REPLACE('da26891b-889f-1673-8188-9f0679a90002', '-', '')), false, true);
insert into feed_image(feed_id, image_id, is_cover, is_present)
values (UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840023', '-', '')),
        UNHEX(REPLACE('da26891b-889f-1673-8188-9f0679a90002', '-', '')), true, true);
insert into feed_image(feed_id, image_id, is_cover, is_present)
values (UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840023', '-', '')),
        UNHEX(REPLACE('da26891b-889f-1673-8188-9f0679a90001', '-', '')), false, true);
/* 해시태그 3개 */
insert into hash_tag(id, tag, feed_id)
values (1, '태그1', UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840002', '-', '')));
insert into hash_tag(id, tag, feed_id)
values (2, '태그2', UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840002', '-', '')));
insert into hash_tag(id, tag, feed_id)
values (3, '태그3', UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840002', '-', '')));

/* 좋아요 3개 */
insert into feed_like(feed_id, profile_id, is_present)
values (UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840002', '-', '')),
        UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840001', '-', '')), true);
insert into feed_like(feed_id, profile_id, is_present)
values (UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840002', '-', '')),
        UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840007', '-', '')), true);
insert into feed_like(feed_id, profile_id, is_present)
values (UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840002', '-', '')),
        UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840009', '-', '')), true);
insert into statistics(counts, type, uuid)
values (3, 'FEED_LIKE', UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840002', '-', '')));

/* 댓글 10개 */
insert into comment(id, content, created_at, is_present, modified_at, feed_id, profile_id)
values (UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840010', '-', '')), '댓글 내용 / test1 유저',
        STR_TO_DATE('20161006 182238', '%Y%m%d %H%i%s'), true, now(),
        UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840002', '-', '')),
        UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840001', '-', '')));
insert into comment(id, content, created_at, is_present, modified_at, feed_id, profile_id)
values (UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840011', '-', '')), '댓글 내용', now(), true,
        now(), UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840002', '-', '')),
        UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840001', '-', '')));
insert into comment(id, content, created_at, is_present, modified_at, feed_id, profile_id)
values (UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840012', '-', '')), '댓글 내용', now(), true,
        now(), UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840002', '-', '')),
        UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840001', '-', '')));
insert into comment(id, content, created_at, is_present, modified_at, feed_id, profile_id)
values (UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840013', '-', '')), '댓글 내용', now(), true,
        now(), UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840002', '-', '')),
        UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840001', '-', '')));
insert into comment(id, content, created_at, is_present, modified_at, feed_id, profile_id)
values (UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840014', '-', '')), '댓글 내용', now(), true,
        now(), UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840002', '-', '')),
        UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840007', '-', '')));
insert into comment(id, content, created_at, is_present, modified_at, feed_id, profile_id)
values (UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840015', '-', '')), '댓글 내용', now(), true,
        now(), UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840002', '-', '')),
        UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840007', '-', '')));
insert into comment(id, content, created_at, is_present, modified_at, feed_id, profile_id)
values (UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840016', '-', '')), '댓글 내용', now(), true,
        now(), UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840002', '-', '')),
        UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840007', '-', '')));
insert into comment(id, content, created_at, is_present, modified_at, feed_id, profile_id)
values (UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840017', '-', '')), '댓글 내용', now(), true,
        now(), UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840002', '-', '')),
        UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840009', '-', '')));
insert into comment(id, content, created_at, is_present, modified_at, feed_id, profile_id)
values (UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840018', '-', '')), '댓글 내용', now(), true,
        now(), UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840002', '-', '')),
        UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840009', '-', '')));
insert into comment(id, content, created_at, is_present, modified_at, feed_id, profile_id)
values (UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840019', '-', '')), '댓글 내용', now(), true,
        now(), UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840002', '-', '')),
        UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840009', '-', '')));
insert into statistics(counts, type, uuid)
values (10, 'COMMENT', UNHEX(REPLACE('da26891b-88b3-1530-8188-b3956c840002', '-', '')));