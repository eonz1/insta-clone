package com.cgram.prom.domain.user.repository;

import com.cgram.prom.domain.user.domain.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndIsPresent(String email, boolean isPresent);

    Optional<User> findByIdAndIsPresent(UUID id, boolean isPresent);
}
