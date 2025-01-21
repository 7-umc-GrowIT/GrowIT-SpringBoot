package umc.GrowIT.Server.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import umc.GrowIT.Server.jwt.JwtTokenUtil;


@Tag(name = "Test", description = "테스트 관련 API")
@RestController
@RequiredArgsConstructor
@Slf4j
public class TestController {

    private final JwtTokenUtil jwtTokenUtil;
    public String accessToken;

    @GetMapping("/test")
    public Long test(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("authentication : {}", authentication);
        log.info("principal : {}", authentication.getPrincipal());
        Long id = (Long) authentication.getPrincipal();
        return id;
    }
}
