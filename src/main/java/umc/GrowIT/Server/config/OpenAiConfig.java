package umc.GrowIT.Server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OpenAiConfig {
    @Value("${openai.api.key}")
    private String openAiKey;

    @Value("${openai.api.key-100}")
    private String openAiKey100;

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
    public RestTemplate keywordModelTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + openAiKey100);
            return execution.execute(request, body);
        });
        return restTemplate;
    }
}
