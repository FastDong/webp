package ce.mnu.wptc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class WptcApplication {


	public static void main(String[] args) {
		SpringApplication.run(WptcApplication.class, args);
	}

}
