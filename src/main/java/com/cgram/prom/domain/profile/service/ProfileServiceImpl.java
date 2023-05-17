package com.cgram.prom.domain.profile.service;

import com.cgram.prom.domain.following.service.FollowService;
import com.cgram.prom.domain.image.domain.Image;
import com.cgram.prom.domain.image.service.ImageService;
import com.cgram.prom.domain.profile.domain.Profile;
import com.cgram.prom.domain.profile.repository.ProfileRepository;
import com.cgram.prom.domain.profile.request.UpdateProfileServiceDto;
import com.cgram.prom.domain.user.domain.User;
import com.cgram.prom.domain.user.service.UserService;
import jakarta.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final FollowService followService;
    private final UserService userService;
    private final ProfileRepository profileRepository;
    private final ImageService imageService;

    @Override
    public void follow(String followedId, String userId) {
        User followedUser = userService.getUserByIdAndIsPresent(followedId);
        User user = userService.getUserByIdAndIsPresent(userId);

        followService.follow(followedUser, user);
    }

    @Override
    public void unfollow(String followedId, String userId) {
        User followedUser = userService.getUserByIdAndIsPresent(followedId);
        User user = userService.getUserByIdAndIsPresent(userId);

        followService.unfollow(followedUser, user);
    }

    @Override
    @Transactional
    public void updateProfile(UpdateProfileServiceDto dto) {
        Optional<Profile> profile = profileRepository.findByUserId(dto.getUserId());
        Image image;
        if (dto.getImage() != null) {
            try {
                File imageFile = transferMultipartFileToFile(dto.getImage());
                image = imageService.saveImage(imageFile);
            } catch (IOException e) {
                throw new RuntimeException("이미지 처리에 실패하였습니다.");
            }
        } else {
            image = null;
        }

        profile.ifPresent(x -> x.update(dto, image));
    }

    @Override
    public void getFeeds(String id) {

    }

    private File transferMultipartFileToFile(MultipartFile mFile) throws IOException {
        String path = System.getProperty("user.dir") + "/src/main/resources/static";
        File file = new File(path + File.separator + mFile.getOriginalFilename());
        mFile.transferTo(file);
        
        return file;
    }
}
