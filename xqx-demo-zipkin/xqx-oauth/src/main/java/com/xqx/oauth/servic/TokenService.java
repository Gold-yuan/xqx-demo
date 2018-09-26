package com.xqx.oauth.servic;

import java.util.HashSet;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xqx.base.exception.ServiceException;
import com.xqx.base.vo.Token;
import com.xqx.base.vo.UserInfo;

/**
 * token服务接口
 */
public interface TokenService {
	// 黑名单，设置初始容量
    static final Set<Long> BLACK_LIST = new HashSet<>(16000);

    Token createToken(String name, String password) throws ServiceException;

    Token createToken(UserInfo userInfo) throws ServiceException;
    
    Token refreshToken(String oldRefreshToken) throws ServiceException;

    UserInfo verifyToken(String accessToken) throws ServiceException;

    void addBlackList(Long userId) throws ServiceException;
    
    void removeBlackList(Long userId) throws ServiceException;
}
