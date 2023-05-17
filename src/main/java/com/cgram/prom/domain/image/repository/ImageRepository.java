package com.cgram.prom.domain.image.repository;

import com.cgram.prom.domain.image.domain.Image;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, UUID> {

}
