package test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(BarConfigurationB.class)
public class BarConfigurationA {

	@Bean
	public String bar() {
		return "barFromA";
	}

}
