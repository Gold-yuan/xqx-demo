package com.xqx.demo.cat.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Event;
import com.dianping.cat.message.Transaction;

@RestController
public class UserController {
	private static Logger logger = LoggerFactory.getLogger(UserController.class);

	@CatAnnotation
	@RequestMapping(value = "/cataop")
	public String catAop(String num) throws IOException {

		if ("1".equals(num)) {
			throw new IOException("自定义的异常");
		}
		return "ok";
	}

	@RequestMapping(value = "/cat")
	public String getAllUser() {
		logger.info("系统时间{}", System.currentTimeMillis());
		String pageName = "pageName12";
		String serverIp = "127.0.0.2";
		double amount = 20;
		Transaction t = Cat.newTransaction("URL2", pageName);
		try {
			// 记录一个业务指标，记录支付金额
			Cat.logMetricForSum("PayAmount12", amount);
			// 记录一个事件，记录具有给定状态和给定数据的事件。
			Cat.logEvent("URL.Server2", "serverIp2", Event.SUCCESS, "ip=${serverIp}" + serverIp);
			// 记录具有成功状态和空数据的事件。
			Cat.logEvent("URL.Server3", "serverIp3");
			// 记录一个业务指标，记录次数 我们每秒聚集一次。
			// 例如，如果您在一秒钟内调用了3次计数（具有相同的名称），我们将只汇总它们的值并向服务器报告一次。
			// 在持续时间的情况下，我们使用平均值而不是汇总值。
			Cat.logMetricForCount("metric.key");
			Cat.logMetricForCount("metric.key", 3);
			Cat.logMetricForDuration("metric.key", 5);

			t.addData("content2");
			t.setStatus(Transaction.SUCCESS);
			if ("1".equals("1")) {
//				throw new RuntimeException("aaa");
			}
			return "ok";
		} catch (Exception e) {
			t.setStatus(e);
			// 使用错误堆栈信息记录错误。
			// 错误是一个特殊事件，它的类型取决于给定Throwable e的类。
			// 如果e instanceof Error，则类型将设置为Error。
			// 否则，如果e是RuntimeException的实例，则该类型将设置为RuntimeException。
			// 在其他情况下，该类型将设置为Exception。
			Cat.logError(e);
		} finally {
			t.complete();
		}
		return "success";
	}
}
