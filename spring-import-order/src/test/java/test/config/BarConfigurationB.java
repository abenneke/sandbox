package test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BarConfigurationB {

	@Bean
	public String bar() {
		return "barFromB";
	}

}
