package umc.GrowIT.Server.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import umc.GrowIT.Server.apiPayload.code.ErrorReasonDTO;
import umc.GrowIT.Server.apiPayload.code.status.ErrorStatus;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        ErrorStatus errorStatus = (ErrorStatus) request.getAttribute("errorStatus");
        ErrorReasonDTO errorReasonDTO = errorStatus.getReasonHttpStatus();

        //TODO: errorStatus NullPointerException 처리

        response.setStatus(errorReasonDTO.getHttpStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(objectMapper.writeValueAsString(errorReasonDTO));
    }
}
