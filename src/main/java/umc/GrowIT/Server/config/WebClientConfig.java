package umc.GrowIT.Server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient kakaoAuthWebClient() {
        return WebClient.builder()
                .baseUrl("https://kauth.kakao.com")
                .build();
    }

    @Bean
    public WebClient kakaoApiWebClient() {
        return WebClient.builder()
                .baseUrl("https://kapi.kakao.com")
                .build();
    }
}
