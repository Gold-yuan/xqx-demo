package com.xqx.demo.cat.controller;

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

	@RequestMapping(value = "/cat")
	public String getAllUser() {
		logger.info("系统时间{}", System.currentTimeMillis());
		String pageName = "pageName12";
		String serverIp = "127.0.0.2";
		double amount = 20;
		Transaction t = Cat.newTransaction("URL12", pageName);
		try {
			// 记录一个事件
			Cat.logEvent("URL.Server12", serverIp, Event.SUCCESS, "ip=" + serverIp + "&name=1");
			// 记录一个业务指标，记录次数
			Cat.logMetricForCount("PayCount12");
			// 记录一个业务指标，记录支付金额
			Cat.logMetricForSum("PayAmount12", amount);

			// 记录一个事件
			Cat.logEvent("URL.Server13", serverIp, Event.SUCCESS, "ip=" + serverIp + "&name=1");
			// 记录一个业务指标，记录次数
			Cat.logMetricForCount("PayCount13");
			// 记录一个业务指标，记录支付金额
			Cat.logMetricForSum("PayAmount13", amount);

			Transaction t2 = Cat.newTransaction("URL14", pageName);

			t2.complete();
			Transaction t3 = Cat.newTransaction("URL15", pageName);

			// 记录一个事件
			Cat.logEvent("URL.Server16", serverIp, Event.SUCCESS, "ip=" + serverIp + "&name=1");
			// 记录一个业务指标，记录次数
			Cat.logMetricForCount("PayCount16");
			// 记录一个业务指标，记录支付金额
			Cat.logMetricForSum("PayAmount16", amount);
			t3.complete();

			t.setStatus(Transaction.SUCCESS);
			return "ok";
		} catch (Exception e) {
			t.setStatus(e);
		} finally {
			t.complete();
		}
		return null;
	}
}
