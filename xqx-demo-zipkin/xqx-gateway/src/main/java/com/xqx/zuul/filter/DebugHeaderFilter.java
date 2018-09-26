package com.xqx.zuul.filter;

import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class DebugHeaderFilter extends ZuulFilter {
	
	private static Logger logger = LoggerFactory.getLogger(DebugHeaderFilter.class);
	
	// zuul.include.debug.header
	boolean INCLUDE_DEBUG_HEADER = true;
	// zuul.include-route-url-header
	boolean INCLUDE_ROUTE_URL_HEADER = true;
	
    @Override
	public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return 10;
    }

    @Override
    public boolean shouldFilter() {
        return INCLUDE_DEBUG_HEADER;
    }

    @Override
    public Object run() {
    	
    	logger.info("DebugHeaderFilter Post 10");
    	
        addStandardResponseHeaders();
        return null;
    }

    void addStandardResponseHeaders() {
    	HttpServletResponse res = RequestContext.getCurrentContext().getResponse();

        RequestContext context = RequestContext.getCurrentContext();
        res.setHeader("X_ZUUL", "xqx_gateway");
        res.setHeader("CONNECTION", "KEEP_ALIVE");
        res.setHeader("X_ZUUL_FILTER_EXECUTION_STATUS", context.getFilterExecutionSummary().toString());
        res.setHeader("X_ORIGINATING_URL", getOriginatingURL());
        if (INCLUDE_ROUTE_URL_HEADER) {
            URL routeUrl = context.getRouteHost();
            if (routeUrl != null) {
                res.setHeader("x-zuul-route-url", routeUrl.toString());
            }
        }

        //Support CORS
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Headers","Content-Type, Accept");

        res.setHeader("x-zuul-remote-call-cost", String.valueOf(RequestContext.getCurrentContext().get("remoteCallCost")));

        if (context.getResponseStatusCode() >= 400) {
            res.setHeader("X_ZUUL_ERROR_CAUSE", "Error from Origin");
            
        }

        if (INCLUDE_DEBUG_HEADER) {
            String debugHeader = "";
            List<String> rd = (List<String>) context.get("routingDebug");
            for(String str : rd) {
            	debugHeader+=str+";";
            }
            res.setHeader("X-Zuul-Debug-Header", debugHeader);
        }
        
        
    }

    String getOriginatingURL() {
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();

        String protocol = request.getHeader("X_FORWARDED_PROTO");
        if (protocol == null) {
        	protocol = "http";
        }
        String host = request.getHeader("HOST");
        String uri = request.getRequestURI();
        String URL = protocol+"://"+host+uri;
        if (request.getQueryString() != null) {
            URL += "?"+request.getQueryString();
        }
        return URL;
    }

  
}
