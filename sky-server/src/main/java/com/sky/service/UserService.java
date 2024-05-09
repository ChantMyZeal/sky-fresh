package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.vo.UserLoginVO;

public interface UserService {

    /**
     * 微信登录
     *
     * @param userLoginDTO 用户登录DTO
     * @return 返回用户登录VO
     */
    UserLoginVO login(UserLoginDTO userLoginDTO);

    /**
     * 退出登录
     */
    void logout();

    /**
     * 用户注册
     *
     * @param userLoginDTO 用户登录DTO
     */
    void register(UserLoginDTO userLoginDTO);

}
