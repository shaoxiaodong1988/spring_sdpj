package com.shendu.dao;

import com.shendu.base.BaseDAO;
import com.shendu.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by huangxinguang on 2017/5/23 上午9:52.
 */
public interface UserDAO extends BaseDAO<User>{
    /**
     * 通过用户名查询用户信息
     * @param userName
     * @return
     */
    User findUserByName(String userName);
    /**
     * 查询用户列表
     * @param pageNo
     * @param pageSize
     * @param userName
     * @return
     */
    List<User> findUserListPage(@Param("pageNum") Integer pageNo,@Param("pageSize") Integer pageSize, @Param("userName") String userName);
}
