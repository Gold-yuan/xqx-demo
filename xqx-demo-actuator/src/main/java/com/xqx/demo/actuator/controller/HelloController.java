package com.xqx.demo.actuator.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@RequestMapping("/hello")
	public String greet() throws IOException {
		String e = Base64Utils.encodeToString(StringUtils.getBytesUtf8("taifu:passw0rd"));
		String encoding = new String(Base64.encodeBase64(StringUtils.getBytesUtf8("taifu:passw0rd")));

		System.out.println(e);
		System.out.println(encoding);
		System.out.println(e == encoding);
		HttpClientUtils client = HttpClientUtils.getInstance();
		Map<String, String> header = new HashMap<>();
		header.put("Authorization", "Basic " + encoding);
		client.setHeader(header);
		String sendHttpGet = client.sendHttpGet("http://localhost:8081/actuator/health");

		return sendHttpGet;
	}

	public static void main(String[] args) throws IOException {

		String encoding = Base64Utils.encodeToString(StringUtils.getBytesUtf8("taifu:passw0rd"));

		HttpClientUtils client = HttpClientUtils.getInstance();
		Map<String, String> header = new HashMap<>();
		header.put("Authorization", "Basic " + encoding);
		client.setHeader(header);
		String sendHttpGet = client.sendHttpGet("http://localhost:10051/actuator/health");
		System.out.println("---"+sendHttpGet);
		 sendHttpGet = client.sendHttpGet("http://localhost:10051/actuator/health");
		System.out.println("==="+sendHttpGet);
	}
}