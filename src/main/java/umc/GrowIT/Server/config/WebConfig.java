package umc.GrowIT.Server.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // 허용할 origin 설정 (Swagger UI 웹 테스트용)
        config.setAllowedOriginPatterns(List.of(
            "http://localhost:8080", 
            "https://growitserver.shop"
        ));

        // 허용할 HTTP 메서드
        config.setAllowedMethods(List.of(
            "GET", 
            "POST", 
            "DELETE", 
            "PATCH"
        ));

        // 허용할 헤더
        config.setAllowedHeaders(List.of(
            "Authorization", 
            "Content-Type"
        ));

        // 인증 정보 허용
        config.setAllowCredentials(true);

        // 모든 경로에 적용
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
