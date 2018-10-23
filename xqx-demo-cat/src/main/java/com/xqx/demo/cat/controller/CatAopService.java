package com.xqx.demo.cat.controller;

import java.io.IOException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;

@Aspect
@Component
public class CatAopService {


    @Around(value = "@annotation(org.springframework.web.bind.annotation.RequestMapping)")
	public Object catTransactionProcesss(ProceedingJoinPoint pjp) {
    	System.out.println("=============================================RequestMapping aop成功");
		Object result = null;
		try {
			result = pjp.proceed();
		} catch (Throwable e) {
			e.printStackTrace();
		}
    	return result;
    }
    
	@Around("@annotation(CatAnnotation)")
	public Object catTransactionProcess(ProceedingJoinPoint pjp) {
    	System.out.println("=============================================CatAnnotation aop成功");
		try {
			Object result = pjp.proceed();
			return result;
		} catch (IOException e) {
			Transaction t = Cat.newTransaction("txkey", "txname");
			Cat.logEvent("URL22.Server3", "serverIp3");
			Cat.logEvent("URL22.Server2", "serverIp2");
			
			
			t.setStatus(e);
			t.complete();
			System.out.println(e.getMessage());
		} catch (Throwable e) {
			System.out.println(e.getMessage());
		} finally {
		}
		return null;
	}
}