package com.xqx.oauth.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;

/**
 * 此注解中包含了
 * @SpringBootApplication
 * @EnableDiscoveryClient   表明开启服务发现
 * @EnableCircuitBreaker    表明开启Hystrix
 */
@SpringCloudApplication
@EnableDiscoveryClient // Eureka客户端
@EnableApolloConfig
@ComponentScan(basePackages = "com.xqx")
public class XqxOauthApplication {

	public static void main(String[] args) {
		SpringApplication.run(XqxOauthApplication.class, args);
	}
	
	@Bean
    @LoadBalanced
    RestTemplate restTemplate(){
        return new RestTemplate();
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
