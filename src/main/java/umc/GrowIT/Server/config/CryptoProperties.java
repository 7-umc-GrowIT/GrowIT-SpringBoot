package umc.GrowIT.Server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "app.crypto")
public record CryptoProperties(int activeKeyId, Map<Integer, String> keys) {}
