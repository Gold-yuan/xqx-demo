package com.xqx.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 异常过滤器
 */
@Component
public class ErrorFilter extends ZuulFilter{

    private static Logger logger = LoggerFactory.getLogger(ErrorFilter.class);

    @Override
    public String filterType() {
        // 异常过滤器
        return "error";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        logger.info("ErrorFilter ======= ERROR ======== 异常过滤器");
        RequestContext requestContext = RequestContext.getCurrentContext();
        
        System.out.println("异常过滤器  ==== " + requestContext.getResponseBody());

        // 过滤请求，不对其路由
        requestContext.setSendZuulResponse(false);
        // 设置返回内容
        requestContext.setResponseBody("出现异常");
        requestContext.getResponse().setContentType("text/html;charset=UTF-8");


        return null;
    }
}
