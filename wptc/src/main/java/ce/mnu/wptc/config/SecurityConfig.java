package ce.mnu.wptc.config;

import ce.mnu.wptc.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/main", "/signup", "/findMyId", "/findMyPw", "/css/**", "/js/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/")
                .loginProcessingUrl("/login") // form action과 일치
                .usernameParameter("email")
                .passwordParameter("passwd")
                .defaultSuccessUrl("/", true)
                .failureUrl("/?error=true") // 로그인 실패 시
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
            )
            .userDetailsService(userDetailsService) // UserDetailsService 등록
            .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
