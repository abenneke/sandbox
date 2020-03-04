package test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FooConfigurationA {

	@Bean
	public String foo() {
		return "fooFromA";
	}
	
}
