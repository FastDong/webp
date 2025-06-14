package ce.mnu.wptc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /images/** URL 패턴으로 요청이 오면
        // file:///C:/dev/uploads/ 경로에서 파일을 찾아 제공하라는 의미
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + uploadDir);
    }
}