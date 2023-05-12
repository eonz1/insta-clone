package com.cgram.prom.domain.verification.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VerificationCodeGeneratorTest {

    private VerificationCodeGenerator generator;

    @BeforeEach
    void Setup(){
        generator = new VerificationCodeGenerator();
    }

    @Test
    void Null이_아니여야_한다() {
        String codes = generator.generateCode();
        assertThat(codes).isNotNull();
    }

    @Test
    void 초기에는_6자리여야_한다() {
        String codes = generator.generateCode();
        assertThat(codes.length()).isEqualTo(6);
    }

    @Test
    void 자리수_변경이_가능해야_한다() {
        generator = new VerificationCodeGenerator(8);
        String codes = generator.generateCode();
        assertThat(codes.length()).isEqualTo(8);
    }
}