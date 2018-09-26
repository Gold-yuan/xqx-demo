package com.xqx.oauth.servic;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.xqx.base.exception.CallRemoteServiceException;
import com.xqx.base.exception.ErrorCode;
import com.xqx.base.vo.ResponseMessage;
import com.xqx.base.vo.UserInfo;

@Component
public class RemoteServiceHandler {
	private static final Logger logger = LoggerFactory.getLogger(TokenServicImpl.class);
	private static final Gson gson = new GsonBuilder().create();
	
	@Autowired
    RestTemplate restTemplate;
	
	private static RemoteServiceHandler remoteServiceHandler;
	
	@PostConstruct
	public void init() {
		remoteServiceHandler = this;
		remoteServiceHandler.restTemplate = this.restTemplate;
	}

	/**
     * 通过User微服务模块校验用户身份
     * @param name
     * @param password
     * @return
     * @throws CallRemoteServiceException
     */
    @HystrixCommand(fallbackMethod = "doLoginFallback")
    public static UserInfo doLogin(String name, String password) throws CallRemoteServiceException {
        // 调用登陆接口
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String, Object>();
        paramMap.add("name", name);
        paramMap.add("password", password);

        String body = remoteServiceHandler.restTemplate.postForEntity("http://XQX-USER/getUser",paramMap, String.class).getBody();

        logger.info("执行登陆结果 == " + body);
        ResponseMessage<?> responseMessage = gson.fromJson(body, ResponseMessage.class);
        if (responseMessage.getStatus() != 0) {
            throw new CallRemoteServiceException(ErrorCode.getErrorCode(responseMessage.getStatus()), responseMessage.getMessage());
        }
        return gson.fromJson(responseMessage.getData().toString(),UserInfo.class);
    }
    @SuppressWarnings("unused")
	private String doLoginFallback(Throwable throwable) {
        return "执行登陆失败 " + throwable.getLocalizedMessage();
    }
    
    
    /**
     * 通过User微服务模块冻结用户
     * @param userId
     * @return
     * @throws CallRemoteServiceException
     */
    @HystrixCommand(fallbackMethod = "doAddBlackListFallback")
    public static Boolean doAddBlackList(Long userId) throws CallRemoteServiceException {
        // 调用登陆接口
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String, Object>();
        paramMap.add("id", userId);

        String body = remoteServiceHandler.restTemplate.postForEntity("http://XQX-USER/doForbidden",paramMap, String.class).getBody();

        logger.info("执行冻结用户结果 == " + body);
        ResponseMessage<?> responseMessage = gson.fromJson(body, ResponseMessage.class);
        if (responseMessage.getStatus() != 0) {
            throw new CallRemoteServiceException(ErrorCode.getErrorCode(responseMessage.getStatus()), responseMessage.getMessage());
        }
        return (Boolean)responseMessage.getData();
    }
    @SuppressWarnings("unused")
	private String doAddBlackListFallback(Throwable throwable) {
        return "冻结用户失败! " + throwable.getLocalizedMessage();
    }
    
    /**
     * 通过User微服务模块冻结用户
     * @param userId
     * @return
     * @throws CallRemoteServiceException
     */
    @HystrixCommand(fallbackMethod = "doRemoveBlackListFallback")
    public static Boolean doRemoveBlackList(Long userId) throws CallRemoteServiceException {
        // 调用登陆接口
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String, Object>();
        paramMap.add("id", userId);

        String body = remoteServiceHandler.restTemplate.postForEntity("http://XQX-USER/doUnforbidden",paramMap, String.class).getBody();

        logger.info("执行解冻用户结果 == " + body);
        ResponseMessage<?> responseMessage = gson.fromJson(body, ResponseMessage.class);
        if (responseMessage.getStatus() != 0) {
            throw new CallRemoteServiceException(ErrorCode.getErrorCode(responseMessage.getStatus()), responseMessage.getMessage());
        }
        return (Boolean)responseMessage.getData();
    }
    @SuppressWarnings("unused")
	private String doRemoveBlackListFallback(Throwable throwable) {
        return "解冻用户失败! " + throwable.getLocalizedMessage();
    }

}
