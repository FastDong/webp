package ce.mnu.wptc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // Spring Security를 활성화하고 웹 보안 설정을 시작함을 알림
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. 요청에 대한 인가(Authorization) 규칙 설정
            .authorizeHttpRequests(authorize -> authorize
                // 아래 경로들은 로그인하지 않은 사용자도 접근 가능
                .requestMatchers("/css/**", "/js/**","/", "/login", "/members/signup", "/css/**", "/js/**", "/images/**").permitAll()
                // 위 경로를 제외한 나머지 모든 경로는 인증(로그인)이 필요함
                .anyRequest().authenticated()
            )
            // 2. 폼 기반 로그인 설정
            .formLogin(form -> form
                // 우리가 만든 커스텀 로그인 페이지 경로를 알려줌
                .loginPage("/login")
                // <form>의 action 속성과 일치시켜야 함. Spring Security가 이 경로로 오는 요청을 처리
                .loginProcessingUrl("/login")
                // 로그인 성공 시 이동할 기본 페이지
                .defaultSuccessUrl("/", true)
                // 로그인 페이지는 모두에게 허용
                .permitAll()
            )
            // 3. 로그아웃 설정
            .logout(logout -> logout
                // 로그아웃 성공 시 이동할 페이지
                .logoutSuccessUrl("/")
                // 로그아웃 시 세션을 무효화시킴
                .invalidateHttpSession(true)
            );

        return http.build();
    }
}