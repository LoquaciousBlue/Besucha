package besucha.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootContextLoader;

public class TestContextLoader extends SpringBootContextLoader {

	@Override
	protected SpringApplication getSpringApplication() {
		return new SpringApplicationBuilder().headless(false).build();
	}
}
