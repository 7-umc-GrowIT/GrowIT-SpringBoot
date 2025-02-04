package umc.GrowIT.Server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import umc.GrowIT.Server.apiPayload.exception.CustomAuthenticationEntryPoint;
import umc.GrowIT.Server.filter.JwtAuthenticationFilter;
import umc.GrowIT.Server.service.userService.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService customUserDetailsService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CorsConfigurationSource corsConfigurationSource;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, CustomUserDetailsService customUserDetailsService, CustomAuthenticationEntryPoint customAuthenticationEntryPoint, CorsConfigurationSource corsConfigurationSource) { //DB 기반 인증에 사용할 CustomUserDetailsService 를 주입받아 준비
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.customUserDetailsService = customUserDetailsService;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.corsConfigurationSource = corsConfigurationSource;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService) //DaoAuthenticationProvider 가 CustomUserDetailsService 를 사용해 DB 에서 사용자 정보 조회하도록 지정
                .passwordEncoder(passwordEncoder()); //DaoAuthenticationProvider 가 BCryptPasswordEncoder 를 사용해 비밀번호를 검증하도록 지정

        return authenticationManagerBuilder.build();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //csrf, formLogin, httpBasic 비활성화 (REST API 서버에서 JWT 사용 시 비활성화)
        http
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource));

        //인증 없이 접근 가능한 URL 설정
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() //인증 없이 접근 가능
                        .requestMatchers("/auth/**", "/users/password", "/terms").permitAll() //인증 없이 접근 가능
                        .anyRequest().authenticated() //나머지 요청은 인증 필요
                );

        //필터에서 예외 발생 시 처리
        http
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(customAuthenticationEntryPoint));

        //JWT 인증 필터 추가
        http
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build(); //설정을 적용한 후 SecurityFilterChain 객체 반환
    }

}
