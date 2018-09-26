package com.xqx.user.entity.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.xqx.base.vo.UserInfo;
import com.xqx.user.entity.User;

public class EntityConverter {
	
	public static UserInfo toUserInfo(User user) {
		if(user == null) {
			throw new NullPointerException("user对象不能为空");
		}
		return new UserInfo(user.getId(),user.getName(),user.getPassword(),user.getForbidden());
	}
	
	public static List<UserInfo> toUserInfoList(Collection<User> userList) {
		if(userList == null || userList.isEmpty()) {
			throw new NullPointerException("userList对象不能为空");
		}
		List<UserInfo> userInfoList = new ArrayList<>();
		userList.stream().forEach(u -> userInfoList.add(toUserInfo(u)));
		return userInfoList;
	}

}
