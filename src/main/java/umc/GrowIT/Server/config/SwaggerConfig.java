package umc.GrowIT.Server.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI GrowITAPI() {
        Info info = new Info()
                .title("UMC 7th GrowIT API")
                .description("UMC 7th GrowIT API 명세서")
                .version("1.0.0");

        // API 요청헤더에 인증정보 포함
        String jwtSchemeName = "JWT TOKEN";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);

        // SecuritySchemes 등록
        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName)
                        .type(SecurityScheme.Type.HTTP) // HTTP 방식
                        .scheme("bearer")
                        .bearerFormat("JWT"));

        // 여러 서버 환경 설정
        Server localServer = new Server()
                .url("http://localhost:8080")
                .description("local");

        Server devServer = new Server()
                .url("https://dev.growitserver.shop")
                .description("dev");

        Server prodServer = new Server()
                .url("https://growitserver.shop")
                .description("prod");

        return new OpenAPI()
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(components)
                .servers(List.of(localServer, devServer, prodServer));
    }
}
