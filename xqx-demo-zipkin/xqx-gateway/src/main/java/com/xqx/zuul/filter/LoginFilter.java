package com.xqx.zuul.filter;

import com.xqx.base.exception.ErrorCode;
import com.xqx.base.vo.ResponseMessage;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 定义请求过滤规则。
 */

@Component
public class LoginFilter extends BaseFilter{

    private static Logger logger = LoggerFactory.getLogger(LoginFilter.class);
    

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
        return RequestContext.getCurrentContext().getRequest().getRequestURL().toString().contains("/login");
    }

    /**
     * 过滤器的具体逻辑。
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        logger.info("LoginFilter Pre 0");
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        if("GET".equals(request.getMethod())) {
        	getErrorRequsetContext(requestContext,401,
                    ResponseMessage.fail(ErrorCode.HTTP_ERROR.getCode(),"请使用POST方式登陆"));
        }
        HttpServletResponse response = requestContext.getResponse();
        logger.info("Send {} reqeust to {}",request.getMethod(), request.getRequestURL().toString());

        // 获取指定参数
        Object name = request.getParameter("name");
        Object password = request.getParameter("password");

        // 设置编码格式
        response.setContentType("text/html;charset=UTF-8");
        if(name == null){
            getErrorRequsetContext(requestContext,401,
                    ResponseMessage.fail(ErrorCode.ILLEGAL_ARGUMENT.getCode(),"参数Name不能为空"));
        }
        if(password == null){
            getErrorRequsetContext(requestContext,401,
                    ResponseMessage.fail(ErrorCode.ILLEGAL_ARGUMENT.getCode(),"参数Password不能为空"));
        }
        return null;
    }
}
