package com.shendu.service;

import com.shendu.model.User;
import com.github.pagehelper.PageInfo;

/**
 * Created by huangxinguang on 2017/5/22 下午5:48.
 */
public interface UserService {

    PageInfo<User> queryUserListPage(Integer pageNo,Integer pageSize,String userName);

    void saveUser(User user);

    User queryUser(Integer userId);

    boolean  check(String userName,String password);

    boolean exists(String userName);

    void deleteUser(Integer userId);
}
