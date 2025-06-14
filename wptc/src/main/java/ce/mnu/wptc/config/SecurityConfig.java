package ce.mnu.wptc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // CSRF 보호 기능 비활성화 (테스트를 위해 잠시 끄기)
            .csrf(csrf -> csrf.disable())
            
            // 모든 HTTP 요청에 대해 접근을 허용
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/**").permitAll() // "/**"는 모든 경로를 의미
            )
            
            // Spring Security의 기본 폼 로그인 기능 비활성화
            .formLogin(form -> form.disable())
            
            // Spring Security의 기본 HTTP Basic 인증 비활성화
            .httpBasic(basic -> basic.disable());

        return http.build();
    }
}