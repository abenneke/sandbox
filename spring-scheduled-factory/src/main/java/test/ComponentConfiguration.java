package test;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class ComponentConfiguration {
	
	@Bean
	public static TaskScheduler taskScheduler() {
		return new ThreadPoolTaskScheduler();
	}
	
	@Bean
	public static SlowDestroyBean slowDestroyBean() {
		return new SlowDestroyBean();
	}
	
	@Bean
	public static ScheduledBean normalScheduledBean() {
		return new ScheduledBean("NormalScheduledBean");
	}
	
	private static class ScheduledBeanFactory extends AbstractFactoryBean<ScheduledBean> {
		
		@Override
		protected ScheduledBean createInstance() throws Exception {
			return new ScheduledBean("ScheduledBeanCreatedByFactory");
		}

		public Class<?> getObjectType() {
			return ScheduledBean.class;
		}
	}
	
	@Bean
	public static FactoryBean<ScheduledBean> scheduledBeanCreatedByFactory() {
		return new ScheduledBeanFactory(); 
	}

}
