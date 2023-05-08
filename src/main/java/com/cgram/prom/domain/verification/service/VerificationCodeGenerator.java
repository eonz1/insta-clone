package com.cgram.prom.domain.verification.service;

import java.util.Random;
import org.springframework.stereotype.Component;

@Component
public class VerificationCodeGenerator {
    private int length;

    public VerificationCodeGenerator() {
        this(6);
    }

    public VerificationCodeGenerator(int length) {
        this.length = length;
    }

    public String generateCode() {
        char[] characterTable = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };

        Random random = new Random(System.currentTimeMillis());
        int tableLength = characterTable.length;
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < length; i++) {
            sb.append(characterTable[random.nextInt(tableLength)]);
        }

        return sb.toString();
    }
}
