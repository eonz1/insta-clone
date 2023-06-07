package com.cgram.prom.domain.profile.domain;

import com.cgram.prom.domain.image.domain.Image;
import com.cgram.prom.domain.profile.request.UpdateProfileServiceDto;
import com.cgram.prom.domain.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.UuidGenerator.Style;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile {

    @Id
    @GeneratedValue
    @UuidGenerator(style = Style.TIME)
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
    private Image image;

    @Builder
    public Profile(UUID id, User user, String intro, Image image) {
        this.id = id;
        this.user = user;
        this.intro = intro;
        this.image = image;
        this.isPublic = true;
    }

    public void update(UpdateProfileServiceDto dto, Image image) {
        this.intro = dto.getIntro() == null ? this.intro : dto.getIntro();
        this.isPublic = dto.getIsPublic() == null ? this.isPublic : dto.getIsPublic();
        this.image = image == null ? this.image : image;
    }
}
