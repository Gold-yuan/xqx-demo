package com.xqx.dashboard.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;

@EnableApolloConfig
@EnableHystrixDashboard
@SpringBootApplication
public class XqxHystrixDashboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(XqxHystrixDashboardApplication.class, args);
	}
}
