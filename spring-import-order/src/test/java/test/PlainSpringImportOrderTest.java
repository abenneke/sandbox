package test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class PlainSpringImportOrderTest extends BaseImportOrderTest {

	@Configuration
	@ComponentScan(basePackages = "test.config")
	public static class ImportOrderTestConfiguration {
	}

}
