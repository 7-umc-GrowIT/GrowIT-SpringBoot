package umc.GrowIT.Server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class DiscordConfig {

    @Bean(name = "discordRestTemplate")
    public RestTemplate discordRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate;
    }
}
