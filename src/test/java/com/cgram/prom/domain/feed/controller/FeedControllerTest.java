package com.cgram.prom.domain.feed.controller;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.cgram.prom.domain.feed.request.DeleteFeedServiceDto;
import com.cgram.prom.domain.feed.request.PostFeedRequest;
import com.cgram.prom.domain.feed.request.PostFeedServiceDto;
import com.cgram.prom.domain.feed.service.FeedService;
import com.cgram.prom.global.security.jwt.filter.WithAuthentication;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = FeedController.class)
class FeedControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    FeedService feedService;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMultipartFile imageFile;
    private MockMultipartFile imageFile2;

    @BeforeEach
    void setup() throws Exception {
        String filePath = System.getProperty("user.dir") + "/src/test/resources";
        imageFile = new MockMultipartFile("images", "mongja.png", "image/png",
            new FileInputStream(
                filePath + File.separator + "mongja.png"));

        imageFile2 = new MockMultipartFile("images", "test.png", "image/png",
            new FileInputStream(filePath + File.separator + "test.png"));
    }

    @Test
    @DisplayName("피드 작성 시 해시태그 리스트 30개 넘으면 예외 발생")
    @WithAuthentication(name = "8df2c1f7-1bcc-4220-a01e-9dad95e87895")
    public void postFeedContentLengthValidation() throws Exception {
        // given
        Set<String> hashtags = new HashSet<>();
        for (int i = 0; i < 33; i++) {
            hashtags.add("#tag" + (i + 1));
        }
        PostFeedRequest request = PostFeedRequest.builder().hashTags(hashtags).build();
        MockMultipartFile multipartFileJson = new MockMultipartFile("feed", "", "application/json",
            objectMapper.writeValueAsBytes(request));

        // expected
        mockMvc
            .perform(multipart("/api/v1/feeds")
                .file(imageFile)
                .file(multipartFileJson)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.validation.hashTags").value("해시태그 최대 개수를 초과했습니다.(30개)"))
            .andDo(print());
    }

    @Test
    @DisplayName("피드 작성 시 이미지 없으면 예외 발생")
    @WithAuthentication(name = "8df2c1f7-1bcc-4220-a01e-9dad95e87895")
    public void postFeedRequiredImages() throws Exception {
        // given
        PostFeedRequest request = PostFeedRequest.builder().content("가나다라마바").build();
        MockMultipartFile multipartFileJson = new MockMultipartFile("feed", "", "application/json",
            objectMapper.writeValueAsBytes(request));

        // expected
        mockMvc
            .perform(multipart("/api/v1/feeds")
                .file(multipartFileJson)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isBadRequest())
            .andDo(print());
    }

    @Test
    @DisplayName("피드 작성 시 피드 없으면 예외 발생")
    @WithAuthentication(name = "8df2c1f7-1bcc-4220-a01e-9dad95e87895")
    public void postFeedRequiredFeed() throws Exception {
        // given

        // expected
        mockMvc
            .perform(multipart("/api/v1/feeds")
                .file(imageFile)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isBadRequest())
            .andDo(print());
    }

    @Test
    @DisplayName("피드 작성")
    @WithAuthentication(name = "8df2c1f7-1bcc-4220-a01e-9dad95e87895")
    public void postFeed() throws Exception {
        // given
        PostFeedRequest request = PostFeedRequest.builder().content("가나다라마바").build();
        MockMultipartFile multipartFileJson = new MockMultipartFile("feed", "", "application/json",
            objectMapper.writeValueAsBytes(request));

        // expected
        mockMvc
            .perform(multipart("/api/v1/feeds")
                .file(imageFile)
                .file(imageFile2)
                .file(multipartFileJson)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isOk())
            .andDo(print());

        ArgumentCaptor<PostFeedServiceDto> postFeedCaptor = ArgumentCaptor.forClass(
            PostFeedServiceDto.class);
        verify(feedService).post(postFeedCaptor.capture());
        PostFeedServiceDto dto = postFeedCaptor.getValue();

        assertThat(dto.getContent()).isEqualTo("가나다라마바");
        assertThat(dto.getHashtags()).isNull();
        assertThat(dto.getImages().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("이미지 업로드 개수 초과하면 예외 발생")
    @WithAuthentication(name = "8df2c1f7-1bcc-4220-a01e-9dad95e87895")
    public void feedImageLengthException() throws Exception {
        // given
        PostFeedRequest request = PostFeedRequest.builder().content("가나다라마바").build();
        MockMultipartFile multipartFileJson = new MockMultipartFile("feed", "", "application/json",
            objectMapper.writeValueAsBytes(request));

        // expected
        mockMvc
            .perform(multipart("/api/v1/feeds")
                .file(imageFile)
                .file(imageFile2)
                .file(imageFile)
                .file(imageFile)
                .file(imageFile)
                .file(imageFile)
                .file(imageFile)
                .file(imageFile)
                .file(imageFile)
                .file(imageFile)
                .file(imageFile) // 이미지 11개
                .file(multipartFileJson)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("이미지 업로드 개수를 초과하였습니다."))
            .andDo(print());

        verify(feedService, times(0)).post(any(PostFeedServiceDto.class));
    }

    @Test
    @DisplayName("피드 삭제")
    @WithAuthentication(name = "8df2c1f7-1bcc-4220-a01e-9dad95e87895")
    public void deleteFeed() throws Exception {
        // given
        UUID feedId = UUID.randomUUID();

        // when
        mockMvc.perform(delete("/api/v1/feeds/{id}", feedId.toString())
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
            .andExpect(status().isOk())
            .andDo(print());

        // then
        ArgumentCaptor<DeleteFeedServiceDto> deleteFeedCaptor = ArgumentCaptor.forClass(
            DeleteFeedServiceDto.class);
        verify(feedService).delete(deleteFeedCaptor.capture());
        DeleteFeedServiceDto dto = deleteFeedCaptor.getValue();

        assertThat(dto.getUserId()).isEqualTo(
            UUID.fromString("8df2c1f7-1bcc-4220-a01e-9dad95e87895"));
        assertThat(dto.getFeedId()).isEqualTo(feedId);
    }
}