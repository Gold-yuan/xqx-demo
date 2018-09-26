package com.xqx.user.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.xqx.base.exception.ErrorCode;
import com.xqx.base.exception.ServiceException;
import com.xqx.user.dao.UserRepostory;
import com.xqx.user.entity.User;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	UserRepostory userRepostory;

    @Override
    public User saveUser(User user) throws ServiceException {
        if(user == null){
            throw new ServiceException(ErrorCode.ILLEGAL_ARGUMENT,"User对象不能为Null");
        }
        return userRepostory.saveAndFlush(user);
    }

    @Override
    public User getUserByID(Long id) throws ServiceException {
        if(id == null){
            throw new ServiceException(ErrorCode.ILLEGAL_ARGUMENT,"参数ID不能为Null");
        }
        return userRepostory.getOne(id);
    }

    @Override
    public User getUser(String name, String password) throws ServiceException {
        if(name == null){
            throw new ServiceException(ErrorCode.ILLEGAL_ARGUMENT,"参数Name不能为Null");
        }
        if(name.isEmpty()){
            throw new ServiceException(ErrorCode.ILLEGAL_ARGUMENT,"参数Name不能为空");
        }
        if(password == null){
            throw new ServiceException(ErrorCode.ILLEGAL_ARGUMENT,"参数Password不能为Null");
        }
        if(password.isEmpty()){
            throw new ServiceException(ErrorCode.ILLEGAL_ARGUMENT,"参数Password不能为空");
        }
        User user = userRepostory.findByNameAndPassword(name, password);
        if(user == null) {
        	throw new ServiceException(ErrorCode.DAO_NOTFOUND,"未找到用户");
        }
        return user;
    }

    @Override
    public List<User> listUserByName(String name) throws ServiceException {
        if(name == null){
            throw new ServiceException(ErrorCode.ILLEGAL_ARGUMENT,"参数Name不能为Null");
        }
        if(name.isEmpty()){
            throw new ServiceException(ErrorCode.ILLEGAL_ARGUMENT,"参数Name不能为空");
        }
        return userRepostory.findByUserName(name);
    }

    public List<User> listUser() throws ServiceException {
        return userRepostory.findAll();
    }

    @Override
    public Long getCount(String name) throws ServiceException {
    	if(name == null){
            throw new ServiceException(ErrorCode.ILLEGAL_ARGUMENT,"参数Name不能为Null");
        }
        User user = new User();
        user.setName(name);
        Example<User> example = Example.of(user);
        return userRepostory.count(example);
    }

    @Override
    public void deleteUser(Long id) throws ServiceException {
    	if(id == null) {
    		throw new ServiceException(ErrorCode.ILLEGAL_ARGUMENT,"参数ID："+id+"不能为Null");
    	}
		userRepostory.deleteById(id);
    }
    
    
    @Override
    public User doForbidden(Long id)throws ServiceException{
    	try {
			User user = userRepostory.getOne(id);
			user.setForbidden(true);
			return userRepostory.saveAndFlush(user);
		} catch(EntityNotFoundException e){
			throw new ServiceException(ErrorCode.DAO_NOTFOUND,"参数用户ID："+id+"未找到对应的有效记录");
		} catch (Exception e) {
			throw new ServiceException(ErrorCode.DAO_ERROR,"参数用户ID："+id+"设置黑名单失败");
		}
    }
    
    @Override
    public User doUnforbidden(Long id)throws ServiceException{
    	try {
			User user = userRepostory.getOne(id);
			if(user.getForbidden()) {
				user.setForbidden(false);
				return userRepostory.saveAndFlush(user);
			}else {
				logger.info("用户ID {}无需解禁",id);
				return user;
			}
			
		} catch(EntityNotFoundException e){
			throw new ServiceException(ErrorCode.DAO_NOTFOUND,"参数用户ID："+id+"未找到对应的有效记录");
		} catch (Exception e) {
			throw new ServiceException(ErrorCode.DAO_ERROR,"参数用户ID："+id+"解禁失败");
		}
    }
}
