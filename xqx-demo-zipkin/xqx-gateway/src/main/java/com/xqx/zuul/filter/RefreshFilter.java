package com.xqx.zuul.filter;

import com.google.common.base.Strings;
import com.xqx.base.exception.ErrorCode;
import com.xqx.base.vo.ResponseMessage;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

public class RefreshFilter extends BaseFilter{
    private static Logger logger = LoggerFactory.getLogger(RefreshFilter.class);
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        String requestPath = RequestContext.getCurrentContext().getRequest().getRequestURL().toString();
        logger.info("RefreshFilter URL " + requestPath);
        return !requestPath.contains("/refresh");
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        // 获取指定参数
        Object refreshToken = request.getParameter("refreshToken");
        String refreshToken_  = request.getHeader("refreshToken");

        if(refreshToken == null && Strings.isNullOrEmpty(refreshToken_)){
            getErrorRequsetContext(requestContext, 401,
                    ResponseMessage.fail(ErrorCode.ILLEGAL_ARGUMENT.getCode(), "参数RefreshToken不能为空"));
        }
        return null;
    }
}
