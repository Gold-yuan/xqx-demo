package com.xqx.demo.job;

import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;

/**
 * 任务Handler示例（Bean模式）
 *
 * 开发步骤： 1、继承"IJobHandler"：“com.xxl.job.core.handler.IJobHandler”；
 * 2、注册到Spring容器：添加“@Component”注解，被Spring容器扫描为Bean实例；
 * 3、注册到执行器工厂：添加“@JobHandler(value="自定义jobhandler名称")”注解，注解value值对应的是调度中心新建任务的JobHandler属性的值。
 * 4、执行日志：需要通过 "XxlJobLogger.log" 打印执行日志；
 *
 * @author xuxueli 2015-12-19 19:43:36
 */
@JobHandler(value = "demoJobHandler")
@Component
public class DemoJobHandler extends IJobHandler {

	@Override
	public ReturnT<String> execute(String param) throws Exception {
		XxlJobLogger.log("XXL-JOB, Hello World.");

		if (param.equals("3")) {
			int p = Integer.parseInt(param) + 1;
			// httpclient
			HttpClient httpClient = null;
			httpClient = new HttpClient();
			httpClient.setFollowRedirects(false); // Configure HttpClient, for example:
			httpClient.start(); // Start HttpClient

			// request
			Request request = httpClient.newRequest("http://localhost:9062/xxl-job-admin/jobinfo/trigger?id=2&executorParam="+p);
			request.method(HttpMethod.GET);
			request.timeout(5000, TimeUnit.MILLISECONDS);

			// invoke
			ContentResponse response = request.send();
			if (response.getStatus() != HttpStatus.OK_200) {
				XxlJobLogger.log("Http StatusCode({}) Invalid.", response.getStatus());
				return FAIL;
			}
		}

		for (int i = 0; i < 5; i++) {
			XxlJobLogger.log("beat at:" + i);
			TimeUnit.SECONDS.sleep(1);
			System.out.println(i + " " + param);
		}
		return SUCCESS;
	}

}
