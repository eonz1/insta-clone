package com.cgram.prom.domain.profile.domain;

import com.cgram.prom.domain.profile.CustomImage;
import com.cgram.prom.domain.profile.request.UpdateProfileServiceDto;
import com.cgram.prom.domain.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Entity
@NoArgsConstructor
public class Profile {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 150)
    private String intro;
    private boolean isPublic;

    @OneToOne
    @JoinColumn(name = "image_id")
    private CustomImage image;

    @Builder
    public Profile(UUID id, User user, String intro, boolean isPublic, CustomImage image) {
        this.id = id;
        this.user = user;
        this.intro = intro;
        this.isPublic = isPublic;
        this.image = image;
    }

    public void update(UpdateProfileServiceDto dto, CustomImage customImage) {
        this.intro = dto.getIntro() == null ? this.intro : dto.getIntro();
        this.isPublic = dto.getIsPublic() == null ? this.isPublic : dto.getIsPublic();
        this.image = customImage == null ? this.image : customImage;
    }
}
