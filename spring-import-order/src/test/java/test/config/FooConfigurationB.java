package test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(FooConfigurationA.class)
public class FooConfigurationB {

	@Bean
	public String foo() {
		return "fooFromB";
	}
	
}
