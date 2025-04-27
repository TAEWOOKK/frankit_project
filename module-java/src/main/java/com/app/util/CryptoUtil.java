package com.app.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class CryptoUtil {

    @Value("${crypt.algorithm}")
    private String algorithm;

    @Value("${crypt.transformation}")
    private String transformation;

    @Value("${crypt.key}")
    private String key;

    // 암호화 및 복호화 공통 메소드
    private String processCipher(String text, int cipherMode) {
        try {
            Cipher cipher = Cipher.getInstance(transformation);
            SecretKey secretKey = new SecretKeySpec(key.getBytes(), algorithm);
            cipher.init(cipherMode, secretKey);
            byte[] processedBytes = cipher.doFinal(cipherMode == Cipher.ENCRYPT_MODE ? text.getBytes() : Base64.getDecoder().decode(text));
            return cipherMode == Cipher.ENCRYPT_MODE ? Base64.getEncoder().encodeToString(processedBytes) : new String(processedBytes);
        } catch (Exception e) {
            throw new RuntimeException("암호화/복호화 중 오류 발생", e);
        }
    }

    // 암호화 메소드
    public String encrypt(String text) {
        return processCipher(text, Cipher.ENCRYPT_MODE);
    }

    // 복호화 메소드
    public String decrypt(String text) {
        return processCipher(text, Cipher.DECRYPT_MODE);
    }
}
