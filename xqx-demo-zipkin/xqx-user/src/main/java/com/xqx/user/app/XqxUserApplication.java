package com.xqx.user.app;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;

@EnableDiscoveryClient // Eureka客户端
@SpringCloudApplication
@EnableApolloConfig
@ComponentScan(basePackages = "com.xqx")
public class XqxUserApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(XqxUserApplication.class).run(args);
	}
	
	@Bean
	public ServletRegistrationBean<HystrixMetricsStreamServlet> getServlet() {
		HystrixMetricsStreamServlet hystrixServlet = new HystrixMetricsStreamServlet();
		ServletRegistrationBean<HystrixMetricsStreamServlet> registrationBean = new ServletRegistrationBean<HystrixMetricsStreamServlet>(hystrixServlet,"/hystrix.stream");
		registrationBean.setLoadOnStartup(1);
		registrationBean.setName(HystrixMetricsStreamServlet.class.getName());
		return registrationBean; 
	}
}
