package com.xqx.zuul.filter;

import com.xqx.base.gson.GsonUtil;
import com.xqx.base.vo.ResponseMessage;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

public abstract class BaseFilter extends ZuulFilter {

    protected <T> RequestContext getErrorRequsetContext(RequestContext requestContext, int statusCode, ResponseMessage<? extends T> message){
        // 过滤请求，不对其路由
        requestContext.setSendZuulResponse(false);
        // 设置返回的错误码
        requestContext.setResponseStatusCode(statusCode);
        // 设置返回内容
        requestContext.setResponseBody(GsonUtil.toJson(message));
        // 设置编码格式
        requestContext.getResponse().setContentType("text/html;charset=UTF-8");

        return requestContext;
    }
}
