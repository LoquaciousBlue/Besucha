package besucha.backend;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context =
			new SpringApplicationBuilder(BackendApplication.class)
				.headless(false)
				.web(WebApplicationType.NONE)
				.run(args);
	}
}
