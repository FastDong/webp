package ce.mnu.wptc.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
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
            .authorizeHttpRequests(authorize -> authorize
                // 👈 (핵심) 이 부분을 수정합니다.
                // Spring Boot가 제공하는 정적 리소스들의 기본 경로를 모두 허용합니다.
                // 이렇게 하면 /css/**, /js/**, /images/** 등을 한 번에 처리할 수 있습니다.
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                
                // 우리가 만든 페이지 경로들을 허용합니다.
                .requestMatchers("/", "/login", "/members/signup").permitAll()
                
                // 위에서 허용한 경로 외의 모든 요청은 인증(로그인)이 필요합니다.
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
            );

        return http.build();
    }
}