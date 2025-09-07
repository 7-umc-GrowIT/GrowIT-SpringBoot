package umc.GrowIT.Server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OpenAiConfig {
    // 대화&요약용
    @Value("${openai.api.key}")
    private String openAiKey;

    // 감정 분석용
    @Value("${openai.api.key-sub}")
    private String subOpenAiKey;

    @Bean
    public RestTemplate template(){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + openAiKey);
            return execution.execute(request, body);
        });
        return restTemplate;
    }

    /**
     * 일기-감정키워드 추출하는 모델 API 호출 템플릿
     */
    @Bean
    public RestTemplate subTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + subOpenAiKey);
            return execution.execute(request, body);
        });
        return restTemplate;
    }
}
