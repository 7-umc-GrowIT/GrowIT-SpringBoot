package umc.GrowIT.Server.util.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import umc.GrowIT.Server.apiPayload.code.status.ErrorStatus;
import umc.GrowIT.Server.apiPayload.exception.CustomException;
import umc.GrowIT.Server.apiPayload.exception.UserHandler;

public class SecurityUtils {

    public static Long getCurrentUserId() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        // 인증되지 않은 사용자 처리
        if ("anonymousUser".equals(userId)) {
            throw new CustomException(ErrorStatus._UNAUTHORIZED);
        }

        try {
            return Long.valueOf(userId);
        } catch (NumberFormatException e) {
            throw new CustomException(ErrorStatus._UNAUTHORIZED);
        }
    }

}
