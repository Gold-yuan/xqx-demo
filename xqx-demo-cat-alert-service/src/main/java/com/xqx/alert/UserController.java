package com.xqx.alert;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
	@RequestMapping(value = "/cat/alert")
	public String catAop(HttpServletRequest req) throws IOException {
		System.out.println("---------------------------------------");

		Enumeration<String> attributeNames = req.getParameterNames();
		if (attributeNames != null) {
			while (attributeNames.hasMoreElements()) {
				String key = attributeNames.nextElement();
				Object attribute = req.getParameter(key);
				System.out.println(key + "：" + attribute);
			}
		}
		return "200";
	}
}
