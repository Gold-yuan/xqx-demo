package com.xqx.user.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.xqx.user.entity.User;

@Repository
public interface UserRepostory extends JpaRepository<User,Long>{

    @Query("select t from User t where t.name = :name")
    List<User> findByUserName(@Param("name")String name);
    
    User findByNameAndPassword(String name,String password);
    
}
