package test;

import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;

public class TestScheduledAnnotationBeanPostProcessor extends ScheduledAnnotationBeanPostProcessor {
	
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) {
		System.out.println("postProcessAfterInitialization " + bean); 
		return super.postProcessAfterInitialization(bean, beanName);
	}
	
	@Override
	public boolean requiresDestruction(Object bean) {
		System.out.println("requires " + bean);
		return super.requiresDestruction(bean);
	}
	
	@Override
	public void postProcessBeforeDestruction(Object bean, String beanName) {
		System.out.println("postProcessAfterInitialization " + bean); 
		super.postProcessBeforeDestruction(bean, beanName);
	}

}
