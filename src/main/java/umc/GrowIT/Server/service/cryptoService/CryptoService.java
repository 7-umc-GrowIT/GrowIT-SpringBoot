package umc.GrowIT.Server.service.cryptoService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import umc.GrowIT.Server.apiPayload.code.status.ErrorStatus;
import umc.GrowIT.Server.apiPayload.exception.CryptoHandler;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CryptoService {
    private static final String T = "AES/GCM/NoPadding";
    private static final int TAG_BITS = 128;
    private static final int IV_LEN = 12;

    private final Map<Integer, SecretKey> dataKeyring;
    private final Integer activeKeyId;
    private final SecureRandom rng = new SecureRandom();

    @PostConstruct
    void verify() {
        if (dataKeyring == null || dataKeyring.isEmpty())
            throw new CryptoHandler(ErrorStatus.CRYPTO_KEY_MISSING);
        if (!dataKeyring.containsKey(activeKeyId))
            throw new CryptoHandler(ErrorStatus.CRYPTO_NO_ACTIVE_KEY);
    }

    // 평문 → 암호문
    public String encrypt(String plain) {
        if (plain == null) return null;
        try {
            SecretKey key = dataKeyring.get(activeKeyId);

            byte[] iv = new byte[IV_LEN];
            rng.nextBytes(iv);

            Cipher c = Cipher.getInstance(T);
            c.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(TAG_BITS, iv));
            byte[] ct = c.doFinal(plain.getBytes(StandardCharsets.UTF_8));

            byte[] ivPlusCt = new byte[iv.length + ct.length];
            System.arraycopy(iv, 0, ivPlusCt, 0, iv.length);
            System.arraycopy(ct, 0, ivPlusCt, iv.length, ct.length);

            return "v1:" + activeKeyId + ":" + Base64.getEncoder().encodeToString(ivPlusCt);
        } catch (Exception e) {
            throw new CryptoHandler(ErrorStatus.CRYPTO_ENCRYPT_ERROR);
        }
    }

    // 암호문 -> 평문
    public String decrypt(String stored) {
        if (stored == null) return null;

        if (!stored.startsWith("v1:")) return stored; // 기존 평문 호환

        try {
            String[] parts = stored.split(":", 3);
            if (parts.length != 3) throw new CryptoHandler(ErrorStatus.CRYPTO_INVALID_FORMAT);
            int keyId = Integer.parseInt(parts[1]);
            SecretKey key = dataKeyring.get(keyId);
            if (key == null) throw new CryptoHandler(ErrorStatus.CRYPTO_NO_KEY_FOR_ID);

            byte[] ivct = Base64.getDecoder().decode(parts[2]);
            if (ivct.length < IV_LEN + 16) throw new CryptoHandler(ErrorStatus.CRYPTO_CIPHERTEXT_TOO_SHORT);

            byte[] iv = new byte[IV_LEN];
            byte[] ct = new byte[ivct.length - IV_LEN];
            System.arraycopy(ivct, 0, iv, 0, IV_LEN);
            System.arraycopy(ivct, IV_LEN, ct, 0, ct.length);

            Cipher c = Cipher.getInstance(T);
            c.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(TAG_BITS, iv));
            return new String(c.doFinal(ct), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new CryptoHandler(ErrorStatus.CRYPTO_DECRYPT_ERROR);
        }
    }
}
