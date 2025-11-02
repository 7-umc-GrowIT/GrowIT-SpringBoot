package umc.GrowIT.Server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class CryptoConfig {

    @Bean
    public Map<Integer, SecretKey> dataKeyring(CryptoProperties props) {
        return props.keys().entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> {
                    byte[] k = Base64.getDecoder().decode(e.getValue());
                    if (k.length != 32) {
                        throw new IllegalArgumentException("AES-256 키는 Base64 디코딩 후 32바이트여야 합니다.");
                    }
                    return new SecretKeySpec(k, "AES");
                }
        ));
    }

    @Bean
    public Integer activeKeyId(CryptoProperties props) {
        return props.activeKeyId();
    }
}
