package com.cgram.prom.domain.feed.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.cgram.prom.domain.feed.response.FeedListResponse;
import com.cgram.prom.domain.feed.response.FeedResponse;
import com.cgram.prom.global.security.jwt.filter.WithAuthentication;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class FeedQueryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @DisplayName("팔로우 한 사람 피드 조회")
    @WithAuthentication(name = "da26891b-88b3-1530-8188-b3956c840000")
    public void getFeedsByProfile() throws Exception {

        // when
        ResultActions result = mockMvc
            .perform(get("/api/v1/feeds?profile_id=da26891b-88b3-1530-8188-b3956c840001&limit=5"))
            .andDo(print());

        String resultString = result.andReturn().getResponse().getContentAsString();
        FeedListResponse feedListResponse = mapper.readValue(resultString, FeedListResponse.class);

        // then
        Assertions.assertThat(feedListResponse.getFeeds().size()).isEqualTo(5);
        FeedResponse firstFeed = feedListResponse.getFeeds().get(0);

        Assertions.assertThat(firstFeed.getFeedId()).isNotNull();
        Assertions.assertThat(firstFeed.getImages().size()).isEqualTo(2);
        Assertions.assertThat(firstFeed.getCoverImage()).isNotNull();
        Assertions.assertThat(firstFeed.getContent()).isNotNull();

        Assertions.assertThat(firstFeed.getComments().getComments().size()).isEqualTo(3);
        for (int i = 0; i < firstFeed.getComments().getComments().size(); i++) {
            Assertions.assertThat(firstFeed.getComments().getComments().get(i)).isNotNull();
        }
        Assertions.assertThat(firstFeed.getComments().getCount()).isEqualTo(3);

        Assertions.assertThat(firstFeed.getCreatedAt()).isNotNull();
        Assertions.assertThat(firstFeed.getModifiedAt()).isNotNull();
        Assertions.assertThat(firstFeed.getUserId()).isNotNull();
        Assertions.assertThat(firstFeed.getUserEmail()).isNotNull();
        Assertions.assertThat(firstFeed.getProfileImagePath()).isNull();

        Assertions.assertThat(feedListResponse.getNextId()).isNotNull();
    }

    @Test
    @DisplayName("해시태그 피드 조회 테스트")
    @WithAuthentication(name = "da26891b-88b3-1530-8188-b3956c840000")
    public void getFeedsByHashTag() throws Exception {

        // when
        ResultActions result = mockMvc
            .perform(
                get("/api/v1/feeds?profile_id=da26891b-88b3-1530-8188-b3956c840001&tag=태그1&limit=5"))
            .andDo(print());

        String resultString = result.andReturn().getResponse().getContentAsString();
        FeedListResponse feedListResponse = mapper.readValue(resultString, FeedListResponse.class);

        // then
        Assertions.assertThat(feedListResponse.getFeeds().size()).isEqualTo(1);
        FeedResponse firstFeed = feedListResponse.getFeeds().get(0);

        Assertions.assertThat(firstFeed.getFeedId()).isNotNull();
        Assertions.assertThat(firstFeed.getImages().size()).isEqualTo(2);
        Assertions.assertThat(firstFeed.getCoverImage()).isNotNull();
        Assertions.assertThat(firstFeed.getContent()).isNotNull();

        Assertions.assertThat(firstFeed.getComments().getComments().size()).isEqualTo(3);
        for (int i = 0; i < firstFeed.getComments().getComments().size(); i++) {
            Assertions.assertThat(firstFeed.getComments().getComments().get(i)).isNotNull();
        }
        Assertions.assertThat(firstFeed.getComments().getCount()).isEqualTo(3);

        Assertions.assertThat(firstFeed.getCreatedAt()).isNotNull();
        Assertions.assertThat(firstFeed.getModifiedAt()).isNotNull();
        Assertions.assertThat(firstFeed.getUserId()).isNotNull();
        Assertions.assertThat(firstFeed.getUserEmail()).isNotNull();
        Assertions.assertThat(firstFeed.getProfileImagePath()).isNull();

        Assertions.assertThat(feedListResponse.getNextId()).isNull();
    }

    @Test
    @DisplayName("내 피드 조회 테스트")
    @WithAuthentication(name = "da26891b-88b3-1530-8188-b3956c840008")
    public void getFeedsByFollowings() throws Exception {

        // when
        ResultActions result = mockMvc
            .perform(get("/api/v1/feeds?profile_id=da26891b-88b3-1530-8188-b3956c840009"))
            .andDo(print());

        String resultString = result.andReturn().getResponse().getContentAsString();
        FeedListResponse feedListResponse = mapper.readValue(resultString, FeedListResponse.class);

        // then
        Assertions.assertThat(feedListResponse.getFeeds().size()).isEqualTo(1);
        FeedResponse firstFeed = feedListResponse.getFeeds().get(0);

        Assertions.assertThat(firstFeed.getFeedId()).isNotNull();
        Assertions.assertThat(firstFeed.getImages().size()).isEqualTo(1);
        Assertions.assertThat(firstFeed.getCoverImage()).isNotNull();
        Assertions.assertThat(firstFeed.getContent()).isNotNull();

        Assertions.assertThat(firstFeed.getComments().getComments()).isNull();

        Assertions.assertThat(firstFeed.getCreatedAt()).isNotNull();
        Assertions.assertThat(firstFeed.getModifiedAt()).isNotNull();
        Assertions.assertThat(firstFeed.getUserId()).isNotNull();
        Assertions.assertThat(firstFeed.getUserEmail()).isNotNull();
        Assertions.assertThat(firstFeed.getProfileImagePath()).isNull();

        Assertions.assertThat(feedListResponse.getNextId()).isNull();
    }
}
