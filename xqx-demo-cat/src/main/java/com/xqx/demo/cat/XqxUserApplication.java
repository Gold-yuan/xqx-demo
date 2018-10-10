package com.xqx.demo.cat;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient // Eureka客户端
@SpringCloudApplication
public class XqxUserApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(XqxUserApplication.class).run(args);
	}
}
