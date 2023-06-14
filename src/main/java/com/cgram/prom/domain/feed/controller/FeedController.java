package com.cgram.prom.domain.feed.controller;

import com.cgram.prom.domain.feed.exception.FeedException;
import com.cgram.prom.domain.feed.exception.FeedExceptionType;
import com.cgram.prom.domain.feed.request.DeleteFeedServiceDto;
import com.cgram.prom.domain.feed.request.GetFeedsDto;
import com.cgram.prom.domain.feed.request.ModifyFeedRequest;
import com.cgram.prom.domain.feed.request.ModifyFeedServiceDto;
import com.cgram.prom.domain.feed.request.PostFeedRequest;
import com.cgram.prom.domain.feed.request.PostFeedServiceDto;
import com.cgram.prom.domain.feed.response.FeedListResponse;
import com.cgram.prom.domain.feed.service.FeedCommandService;
import com.cgram.prom.domain.feed.service.FeedQueryService;
import com.cgram.prom.domain.image.service.FileConverter;
import jakarta.validation.Valid;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feeds")
public class FeedController {

    private final FeedCommandService feedCommandService;
    private final FeedQueryService feedQueryService;
    private FileConverter fileConverter = new FileConverter();

    @GetMapping("")
    public ResponseEntity<FeedListResponse> getFeeds(Authentication authentication,
        @RequestParam(defaultValue = "12") int limit,
        @RequestParam(required = false) String tag,
        @RequestParam(required = false) String cursor,
        @RequestParam(required = false) String profileId) {

        GetFeedsDto dto = GetFeedsDto.builder()
            .limit(limit)
            .cursor(cursor)
            .tag(tag)
            .profileId(profileId)
            .build();

        FeedListResponse feedListResponse = feedQueryService.getFeeds(dto);

        return ResponseEntity.ok().body(feedListResponse);
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void post(Authentication authentication,
        @RequestPart(value = "images") List<MultipartFile> images,
        @Valid @RequestPart(value = "feed") PostFeedRequest request) {
        if (images.size() > 10) {
            throw new FeedException(FeedExceptionType.OVER_MAX_IMAGES);
        }

        List<File> imageFiles = new ArrayList<>();

        for (MultipartFile multipartFile : images) {
            imageFiles.add(fileConverter.transferMultipartFileToFile(multipartFile));
        }

        PostFeedServiceDto dto = PostFeedServiceDto.builder()
            .id(UUID.fromString(authentication.getName()))
            .hashtags(request.getHashTags())
            .content(request.getContent())
            .images(imageFiles)
            .build();

        feedCommandService.post(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(Authentication authentication, @PathVariable String id) {
        DeleteFeedServiceDto dto = DeleteFeedServiceDto.builder()
            .feedId(UUID.fromString(id))
            .userId(UUID.fromString(authentication.getName()))
            .build();
        feedCommandService.delete(dto);
    }

    @PatchMapping("/{id}")
    public void modify(Authentication authentication, @PathVariable String id,
        @Valid @RequestBody ModifyFeedRequest request) {

        ModifyFeedServiceDto dto = ModifyFeedServiceDto.builder()
            .userId(UUID.fromString(authentication.getName()))
            .feedId(UUID.fromString(id))
            .content(request.getContent())
            .hashTags(request.getHashTags())
            .build();

        feedCommandService.modify(dto);
    }
}
