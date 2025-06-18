package ce.mnu.wptc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

@Configuration
public class EncoderConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Spring Security 5.8+ 기본값 권장
        return Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }
}
