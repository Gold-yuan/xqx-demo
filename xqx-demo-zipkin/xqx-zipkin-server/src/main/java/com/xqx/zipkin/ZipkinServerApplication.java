package com.xqx.zipkin;

import javax.sql.DataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;

import zipkin2.server.internal.EnableZipkinServer;

@SpringCloudApplication
@EnableDiscoveryClient
@EnableApolloConfig
@EnableZipkinServer
public class ZipkinServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZipkinServerApplication.class, args);
	}
}
