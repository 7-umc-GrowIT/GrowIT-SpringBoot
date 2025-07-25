package umc.GrowIT.Server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

//AppStore의 영수증 검증 api호출을 위해 spring이 HTTP 요청을 보내기 위한 Bean 설정
@Configuration
public class RestTemplateConfig {
    
    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10000); // 연결 타임아웃 10초
        factory.setReadTimeout(30000);    // 일기 타임아웃 30초

        return new RestTemplate();
    }

}
