package com.cgram.prom.domain.verification.repository;

import com.cgram.prom.domain.verification.domain.VerificationCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {

    Optional<VerificationCode> findTopByEmailOrderByExpirationDateDesc(String email);
}
