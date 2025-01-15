package umc.GrowIT.Server.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import umc.GrowIT.Server.jwt.JwtTokenUtil;
import umc.GrowIT.Server.service.authService.UserCommandService;
import umc.GrowIT.Server.web.dto.UserDTO.UserRequestDTO;

import java.util.Base64;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TestController {

    private final JwtTokenUtil jwtTokenUtil;
    public String accessToken;

    @GetMapping("/test")
    public String test(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
