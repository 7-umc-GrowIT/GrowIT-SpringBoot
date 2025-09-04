package umc.GrowIT.Server.web.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import umc.GrowIT.Server.apiPayload.ApiResponse;

import java.util.Map;

@RestController
public class TestController {
    @PostMapping("/apple/oauth/return")
    public ApiResponse<Map<String, String>> handleApplePost(@RequestParam Map<String, String> params) {
        return ApiResponse.onSuccess(params);
    }
}
