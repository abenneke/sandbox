package test;

import org.springframework.scheduling.annotation.Scheduled;

public class ScheduledBean {

	private String name;

	public ScheduledBean(String name) {
		this.name = name;
	}

	@Scheduled(fixedDelay = 100, initialDelay = 100)
	public void testScheduled() {
		System.out.println(name + ".testScheduled");
	}

}
