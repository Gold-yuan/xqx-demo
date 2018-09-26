package com.xqx.zuul.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;

@Component
public class ZuulConfig {
	

    @ApolloConfig("filterConfig")
    private Config filterConfig;
    
    /**
     * 是否启Debug模式总开关
     */
    @Value("${zuul.filter.debug.open:false}")
    private Boolean debugOpen;

    /**
     * 是否所有请求均启动Debug模式
     */
    @Value("${zuul.filter.debug.allRequest:false}")
    private Boolean allDebugRequest;

    /**
     * Debug模式中Request的参数
     */
    @Value("${zuul.filter.debug.requestParameter:debug}")
    private String debugParameter;

	public Boolean getDebugOpen() {
		return debugOpen;
	}

	public Boolean getAllDebugRequest() {
		return allDebugRequest;
	}

	public String getDebugParameter() {
		return debugParameter;
	}
}
