package test;

import org.springframework.beans.factory.DisposableBean;

public class SlowDestroyBean implements DisposableBean{

	public void destroy() throws Exception {
		System.out.println("SlowDestroyBean.destroy");

		// simply slow down disposing a bit
		Thread.sleep(500);
	}
	
}
