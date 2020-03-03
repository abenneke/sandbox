package test;

import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.scheduling.annotation.SchedulingConfiguration;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		GenericApplicationContext ctx = new GenericApplicationContext();
		ctx.registerBean(ConfigurationClassPostProcessor.class);
		ctx.registerBean(SchedulingConfiguration.class);
		
		ctx.registerBean(ComponentConfiguration.class);
		ctx.refresh();

		System.out.println("Context started");

		// (also) trigger creation of beans created by factories
		System.out.println(ctx.getBeansOfType(ScheduledBean.class).size() + " ScheduledBeans");

		Thread.sleep(500);

		System.out.println("Context closing");
		ctx.close();
		System.out.println("Context closed");

		Thread.sleep(500);

		System.out.println("Terminating");
	}

}
