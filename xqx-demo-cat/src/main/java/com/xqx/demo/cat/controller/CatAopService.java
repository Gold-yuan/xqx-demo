package com.xqx.demo.cat.controller;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;

@Aspect
@Component
public class CatAopService {

	@Around("@annotation(CatAnnotation)")
	public Object catTransactionProcess(ProceedingJoinPoint pjp) throws Throwable {
		Transaction t = Cat.newTransaction("txkey", "txname");
		try {
			Object result = pjp.proceed();
			t.setStatus(Transaction.SUCCESS);
			return result;
		} catch (Throwable e) {
			t.setStatus(e);
			throw e;
		} finally {
			t.complete();
		}
	}
}