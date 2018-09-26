package com.xqx.turbine.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.turbine.EnableTurbine;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;

@EnableTurbine
@EnableApolloConfig
@EnableDiscoveryClient
@SpringBootApplication
public class XqxTurbineApplication {

	public static void main(String[] args) {
		SpringApplication.run(XqxTurbineApplication.class, args);
	}
}
